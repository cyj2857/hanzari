<template>
  <div>
    <canvas ref="canvas" class="canvas" width="1100px" height="800px"></canvas>
    <input v-show="false" ref="inputUpload" type="file" @change="onFileChange" />
    <v-btn color="success" @click="$refs.inputUpload.click()">File Upload to Background</v-btn>
    <v-btn @click="addVacantBtn" color="primary" dark>Add Vacant</v-btn>
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
        this.currentSelectedFloor = floor;
        this.changeFloor(this.currentSelectedFloor);
      });
  },
  mounted() {
    this.initializing();
  },
  methods: {
    //canvas, map 생성
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
        this.mySeatList = new Map();
      }
    },
    changeFloor(floor) {
      //도형 랜더링 전에 화면의 도형들을  초기화
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });

      //각 층의 저장된 도형 리스트 화면에 뿌려주기
      //현재 층의 이미지가 저장되어있다면
      if (this.myImageList.get(floor) != null) {
        this.loadImage(this.myImageList.get(floor));

        //현재 층에 그린 도형들이 있다면
        if (this.floorSeatList.get(floor)) {
          var onefloorSeatList = this.floorSeatList.get(floor);

          for (var i = 0; i < onefloorSeatList.length; i++) {
            this.myCanvas.add(onefloorSeatList[i]);
            console.log("onefloorSeatList : " + onefloorSeatList[i]);
          }
        }
      } else if (this.myImageList.get(floor) == null) {
        //현재 층의 이미지가 저장되어있지 않다면
        //화면에 그려져있던 이미지와 도형 초기화
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
      console.log(
        "myImageList : " + this.myImageList.get(this.currentSelectedFloor)
      );
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },

    //도형생성시
    makeRectBtn(item) {
      console.log("currnet floor is " + this.currentSelectedFloor);

      //각 층에 해당하는 도형 리스트 리턴하기
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
        console.log("left = " + x.left); //150
      });
      // var asObject = group.toObject(["seatId"]);
      // console.log(asObject.seatId);
      this.myCanvas.add(group);

      //각 층의 도형 리스트에 하나의 해당 도형을 넣기
      mynewSeatList.push(group);
      //각 층의 도형 리스트를 접근 할 수 있는 map에 도형리스트를 저장하기
      //실질적으로 floorSeatList로 각 층의 도형 리스트를 접근한다

      this.floorSeatList.set(
        this.currentSelectedFloor,
        this.mySeatList.get(this.currentSelectedFloor)
      );
      console.log("floorSeatList-size : " + this.floorSeatList.size);

      console.log(
        "floorSeatList : " + this.floorSeatList.get(this.currentSelectedFloor)
      );
    },

    //각 층의 도형 리스트 생성하기
    newSeatList: function(floor) {
      //층에 해당하는 도형리스트가 만들어지지 않았을때
      if (!this.mySeatList.get(floor)) {
        var newSeatsList = new Array();
        this.mySeatList.set(floor, newSeatsList);
        return this.mySeatList.get(floor);
        console.log("newSeatList X " + this.mySeatList.get(floor));
      } else {
        return this.mySeatList.get(floor);
        console.log("newSeatList O " + this.mySeatList.get(floor));
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
      this.mySeatList.get(this.currentSelectedFloor).length = 0;
      if (this.floorSeatList.delete(this.currentSelectedFloor))
        alert("success");
      else alert("fail");
      //그 층의 모든 list 없애기
    },
      deleteBtn() {
      var activeObject = this.myCanvas.getActiveObject();
      //console.log("activeobject : " + activeObject);

      var shapearray = new Array();
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          shapearray.push(obj);
        });
      // console.log("shapearray :  " + shapearray);
      // console.log("shapearray length :  " + shapearray.length);

      if (activeObject) {
        if (confirm("Are you sure?")) {
          shapearray.slice().forEach(obj => {
            if (obj == activeObject) {
              //  console.log("selected activeobject: " + activeObject);
              //  console.log("selected obj : " + obj);
              //delete
              var index = shapearray.indexOf(activeObject);
              shapearray.splice(index, 1);
              //   console.log("after delete shapearray :  " + shapearray);
              //   console.log("arter delte shapearray length :  " + shapearray.length);
            }
          });

          this.myCanvas.remove(activeObject);
          this.mySeatList.get(this.currentSelectedFloor).length = 0;
          //modify map(mySeatList)
          this.floorSeatList.delete(this.currentSelectedFloor);
          this.mySeatList.set(this.currentSelectedFloor, shapearray);
          this.floorSeatList.set(this.currentSelectedFloor,this.mySeatList.get(this.currentSelectedFloor));
          console.log(
            "mySeatList >>>>>" + this.mySeatList.get(this.currentSelectedFloor)
          );
          console.log(
            "floorSeatList >>>>>" +
              this.floorSeatList.get(this.currentSelectedFloor)
          );
          //좌석 지우면 list에 있는거 없애기
        }
      }
    },
    clickSaveBtn() {
      this.$axios.post("/springBootURL/", {}).then(response => {
        this.result = response.data;
      });
    },
    addVacantBtn() {
      console.log("currnet floor is " + this.currentSelectedFloor);

      //각 층에 해당하는 도형 리스트 리턴하기
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
        console.log("left = " + x.left); //150
      });

      this.myCanvas.add(group);

      mynewSeatList.push(group);

      this.floorSeatList.set(
        this.currentSelectedFloor,
        this.mySeatList.get(this.currentSelectedFloor)
      );
      console.log("floorSeatList-size : " + this.floorSeatList.size);

      console.log(
        "floorSeatList : " + this.floorSeatList.get(this.currentSelectedFloor)
      );
    }
  }
};
</script>

<style scoped>
.canvas {
  margin-left: 45px;
  border: 1px solid #000;
  background: aliceblue;
  height: 800px;
  width: 900px;
}
</style>