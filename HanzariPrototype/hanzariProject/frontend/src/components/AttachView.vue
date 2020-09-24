<template>
  <div>
    <canvas ref="canvas" class="canvas" width="1100px" height="800px"></canvas>
    <input v-show="false" ref="inputUpload" type="file" @change="onFileChange" />
    <v-btn color="success" @click="$refs.inputUpload.click()">File Upload to Background</v-btn>
    <v-btn @click="addVacantBtn" color="primary" dark >Add Vacant</v-btn>
    <v-btn @click="deleteBtn">Delete Selected Shape</v-btn>
    <v-btn @click="deleteAllBtn">Delete All Shapes</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import axios from "axios";
export default {
  data: function() {
    return {
      myCanvas: null,
      myImageList: null,
      seatId: 0,
      currentSelectedFloor: null,
      mySeatList: null, //current floor's seat list
      floorSeatList: null //all floor's seat list
    };
  },
  created() {
    eventBus.$on("createdRect", item => {
      this.makeRectBtn(item);
    }),
      eventBus.$on("changeFloor", floor => {
        this.currentSelectedFloor = floor
        this.changeFloor(this.currentSelectedFloor);
      });
  },
  mounted() {
    this.initializing();
  },
  methods: {
    //canvas, map ����
    initializing() {
      if (this.myCanvas == null) {
        const ref = this.$refs.canvas;
        this.myCanvas = new fabric.Canvas(ref);
      }
      if (this.myImageList == null) {
        this.myImageList = new Map();
      }
      if (this.floorSeatList == null) {
        this.floorSeatList = new Map();
      }
      if (this.mySeatList == null) {
        this.mySeatList = new Array();
      }
    },
    changeFloor(floor) {
      //���� ������ ���� ȭ���� ��������  �ʱ�ȭ
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });

      //�� ���� ����� ���� ����Ʈ ȭ�鿡 �ѷ��ֱ�
      //���� ���� �̹����� ����Ǿ��ִٸ�
      if (this.myImageList.get(floor) != null) {
        this.loadImage(this.myImageList.get(floor));

        //���� ���� �׸� �������� �ִٸ�
        if (this.floorSeatList.get(floor)) {
          var onefloorSeatList = this.floorSeatList.get(floor);

          for (var i = 0; i < onefloorSeatList.length; i++) {
            this.myCanvas.add(onefloorSeatList[i]);
          }
        }
      } else if (this.myImageList.get(floor) == null) {//���� ���� �̹����� ����Ǿ����� �ʴٸ� 
        //ȭ�鿡 �׷����ִ� �̹����� ���� �ʱ�ȭ 
        this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });

        this.myCanvas.backgroundImage = 0;
        this.myCanvas.backgroundColor = "aliceblue";
        this.myCanvas.renderAll();
      }
    },
    createImage(file) {
      this.loadImage(file);
      this.saveImage(file);
    },
    loadImage(file) {
      var reader = new FileReader();
      reader.onload = e => {
        fabric.Image.fromURL(e.target.result, img => {
          img.set({
            scaleX: this.myCanvas.width / img.width,
            scaleY: this.myCanvas.height / img.height
          });
          this.myCanvas.setBackgroundImage(
            img,
            this.myCanvas.renderAll.bind(this.myCanvas)
          );
        });
      };
      reader.readAsDataURL(file);
    },
    saveImage(file) {
      this.myImageList.set(this.currentSelectedFloor, file);
      console.log(this.myImageList.get(this.currentSelectedFloor));
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },
    
    //����������
    makeRectBtn(item) {
      console.log("currnet floor is " + this.currentSelectedFloor);
     
      //�� ���� �ش��ϴ� ���� ����Ʈ �����ϱ� 
      var mynewSeatList = this.newSeatList(this.currentSelectedFloor);

      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "blue",
        opacity: 1
      });
      var textObject = new fabric.IText(item.name, {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      });
      var group = new fabric.Group([rectangle, textObject], {
        id: item.employee_id,
        seatId: this.seatId++, // 1,2,3,4
        employee_id: item.employee_id,
        left: 150,
        top: 150
      });
      group.on("mouseover", function(e) {
        var group = e.target;
        group.item(0).set("fill", "red");
        var asObject = group.toObject(["employee_id"]);
        var x = group.toObject(["left"]);

        console.log("employee id = " + asObject.employee_id); //1771354
        console.log("x = " + x.left); //150
      });
      // var asObject = group.toObject(["seatId"]);
      // console.log(asObject.seatId);
      this.myCanvas.add(group);
      
      //�� ���� ���� ����Ʈ�� �ϳ��� �ش� ������ �ֱ�
      mynewSeatList.push(group);
      //�� ���� ���� ����Ʈ�� ���� �� �� �ִ� map�� ��������Ʈ�� �����ϱ�
      //���������� floorSeatList�� �� ���� ���� ����Ʈ�� �����Ѵ� 
      
      this.floorSeatList.set(this.currentSelectedFloor,this.mySeatList.get(this.currentSelectedFloor));
      console.log('���� : ' + this.floorSeatList.size)
      
      console.log(this.floorSeatList.get(this.currentSelectedFloor));
    },

    //�� ���� ���� ����Ʈ �����ϱ�
    newSeatList: function(floor) {
      //���� �ش��ϴ� ��������Ʈ�� ��������� �ʾ�����
      if (!this.mySeatList.get(floor)) {
        var newSeatsList = new Array();
        this.mySeatList.set(floor, newSeatsList);
        return this.mySeatList.get(floor);
      } else {
        return this.mySeatList.get(floor);
      }
    },
    deleteAllBtn() {
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });

      //console.log(this.currentSelectedFloor)
      this.mySeatList.clear()
      if(this.floorSeatList.delete(this.currentSelectedFloor))
        alert('success')
      else
        alert('fail')
      //�� ���� ��� list ���ֱ�
    },
    deleteBtn() {
      var activeObject = this.myCanvas.getActiveObject();
      if (activeObject) {
        if (confirm("Are you sure?")) {
          this.myCanvas.remove(activeObject);
          
        //�¼� ����� list�� �ִ°� ���ֱ�
        }
      }
    },
    clickSaveBtn() {
      this.$axios.post("/springBootURL/", {}).then(response => {
        this.result = response.data;
      });
    },
    addVacantBtn(){
      console.log("currnet floor is " + this.currentSelectedFloor);
     
      //�� ���� �ش��ϴ� ���� ����Ʈ �����ϱ� 
      var mynewSeatList = this.newSeatList(this.currentSelectedFloor);

      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "yellow",
        opacity: 1
      });
      
      var group = new fabric.Group([rectangle], {
        seatId: this.seatId++, // 1,2,3,4
        left: 150,
        top: 150
      });
      group.on("mouseover", function(e) {
        var group = e.target;
        var asObject = group.toObject(["seatId"]);
        var x = group.toObject(["left"]);

        console.log("seatId = " + asObject.seatId); //
        console.log("x = " + x.left); //150
      });

      this.myCanvas.add(group);

      mynewSeatList.push(group);
      
      this.floorSeatList.set(this.currentSelectedFloor,this.mySeatList.get(this.currentSelectedFloor));
      console.log('���� : ' + this.floorSeatList.size)
      
      console.log(this.floorSeatList.get(this.currentSelectedFloor));

    }
  }
};
</script>

<style scoped>
.figureBtn {
  width: 100px;
  height: 100px;
}
.canvas {
  margin-left: 45px;
  border: 1px solid #000;
  background: aliceblue;
  height: 800px;
  width: 900px;
}
</style>