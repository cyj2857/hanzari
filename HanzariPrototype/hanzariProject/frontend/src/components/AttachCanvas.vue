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
      floorCanvas: null,
      floorImageList: null,
      seatId: 0,
      currentSelectedFloor: null,
      eachFloorSeatMap: null, //current floor's seat map
      allFloorsSeatMap: null //all floor's seat map
      //한글 테스트
    };
  },
  created() {
    eventBus.$on("createSeat", item => {
      this.createSeat(item);
    }),
      eventBus.$on("showSeat", item => {
        this.showSeat(item);
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
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        this.floorCanvas = new fabric.Canvas(ref);
      }
      if (this.floorImageList == null) {
        this.floorImageList = new Map();
      }
      if (this.allFloorsSeatMap == null) {
        this.allFloorsSeatMap = new Map();
      }
      if (this.eachFloorSeatMap == null) {
        this.eachFloorSeatMap = new Map();
      }
    },
    changeFloor(floor) {
      //도형 랜더링 전에 화면의 도형들을  초기화
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.floorCanvas.remove(obj);
        });

      //각 층의 저장된 도형 리스트 화면에 뿌려주기
      //현재 층의 이미지가 저장되어있다면
      if (this.floorImageList.get(floor) != null) {
        this.loadImage(this.floorImageList.get(floor));

        //현재 층에 그린 도형들이 있다면
        if (this.allFloorsSeatMap.get(floor)) {
          var myOnefloorSeatList = this.allFloorsSeatMap.get(floor);

          for (var i = 0; i < myOnefloorSeatList.length; i++) {
            this.floorCanvas.add(myOnefloorSeatList[i]);
            console.log("myOnefloorSeatList : " + myOnefloorSeatList[i]);
          }
        }
      } else if (this.floorImageList.get(floor) == null) {
        //현재 층의 이미지가 저장되어있지 않다면
        //화면에 그려져있던 이미지와 도형 초기화
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach(obj => {
            this.floorCanvas.remove(obj);
          });

        this.floorCanvas.backgroundImage = 0;
        this.floorCanvas.backgroundColor = "aliceblue";
        this.floorCanvas.renderAll();
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
            scaleX: this.floorCanvas.width / img.width,
            scaleY: this.floorCanvas.height / img.height
          });
          this.floorCanvas.setBackgroundImage(
            img,
            this.floorCanvas.renderAll.bind(this.floorCanvas)
          );
        });
      };
      reader.readAsDataURL(file);
    },
    saveImage(file) {
      this.floorImageList.set(this.currentSelectedFloor, file);
      console.log(this.floorImageList.get(this.currentSelectedFloor));
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },

    //도형생성시
    createSeat(item) {
      console.log("currnet floor is " + this.currentSelectedFloor);

      //각 층에 해당하는 도형 리스트 리턴하기
      var eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "blue",
        opacity: 1
      });
      var textObject = new fabric.IText(item.name, {
        left: 0,
        top: rectangle.height/3,
        fontSize: 13,
        fill: "#000000"
      });
      var group = new fabric.Group([rectangle, textObject], {
        id: item.employee_id,
        seatId: this.seatId++, // 1,2,3,4
        employee_id: item.employee_id,
        floor_id: this.currentSelectedFloor,
        left: 150,
        top: 150
      });
      group.on("mouseover", function(e) {
        var group = e.target;
        group.item(0).set("fill", "red");
        var asObject = group.toObject(["employee_id"]);
        var x = group.toObject(["left"]);

        console.log("employee id = " + asObject.employee_id); //1771354
        //console.log(asObject.floor_id+"층에 자리가 생성되었습니다.");
        console.log("left = " + x.left); //150
      });
      // var asObject = group.toObject(["seatId"]);
      // console.log(asObject.seatId);
      this.floorCanvas.add(group);

      //각 층의 도형 리스트에 하나의 해당 도형을 넣기
      eachFloorSeatList.push(group);
      //각 층의 도형 리스트를 접근 할 수 있는 map에 도형리스트를 저장하기
      //실질적으로 allFloorsSeatMap로 각 층의 도형 리스트를 접근한다

      this.allFloorsSeatMap.set(
        this.currentSelectedFloor,
        this.eachFloorSeatMap.get(this.currentSelectedFloor)
      );
      console.log("allFloorsSeatMap-size : " + this.allFloorsSeatMap.size);

      console.log(
        "allFloorsSeatMap : " +
          this.allFloorsSeatMap.get(this.currentSelectedFloor)
      );
    },

    showSeat(item) {
      //현재 탭의 층에 대해서만
      var eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      for (var i = 0; i < eachFloorSeatList.length; i++) {
        var myGroup = eachFloorSeatList[i];
        var asObject = myGroup.toObject(["employee_id", "floor_id"]);
        console.log(asObject.floor_id + "층에 자리가 있습니다.");

        if (item.employee_id == asObject.employee_id) {
          this.floorCanvas
            .getObjects()
            .slice()
            .forEach(obj => {
              this.floorCanvas.remove(obj);
            });

          //각 층의 저장된 도형 리스트 화면에 뿌려주기
          //현재 층의 이미지가 저장되어있다면
          if (this.floorImageList.get(this.currentSelectedFloor) != null) {
            this.loadImage(this.floorImageList.get(this.currentSelectedFloor));

            for (var i = 0; i < eachFloorSeatList.length; i++) {
              this.floorCanvas.add(eachFloorSeatList[i]);
            }
          }
          myGroup.item(0).set("fill", "yellow");
        }
        //자리가 아직 없을때 예외처리 하기
        else {
          alert(item.name+"은 자리가 존재하지 않습니다.");
        }
       
      }
    },
    //각 층의 도형 리스트 생성하기
    getEachFloorSeatList: function(floor) {
      //층에 해당하는 도형리스트가 만들어지지 않았을때
      if (!this.eachFloorSeatMap.get(floor)) {
        var newSeatsList = new Array();
        this.eachFloorSeatMap.set(floor, newSeatsList);
        return this.eachFloorSeatMap.get(floor);
        console.log("newSeatList X " + this.eachFloorSeatMap.get(floor));
      } else {
        return this.eachFloorSeatMap.get(floor);
        console.log("newSeatList O " + this.eachFloorSeatMap.get(floor));
      }
    },
    deleteAllBtn() {
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.floorCanvas.remove(obj);
        });

      //console.log(this.currentSelectedFloor)
      this.getEachFloorSeatList(this.currentSelectedFloor).length = 0;
      if (this.allFloorsSeatMap.delete(this.currentSelectedFloor))
        alert("success");
      else alert("fail");
      //그 층의 모든 list 없애기
    },
    deleteBtn() {
      var activeObject = this.floorCanvas.getActiveObject();
      //console.log("activeobject : " + activeObject);

      var shapearray = new Array();
      this.floorCanvas
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

          this.floorCanvas.remove(activeObject);
          this.eachFloorSeatMap.get(this.currentSelectedFloor).length = 0;
          //modify map(eachFloorSeatMap)
          this.allFloorsSeatMap.delete(this.currentSelectedFloor);
          this.eachFloorSeatMap.set(this.currentSelectedFloor, shapearray);
          this.allFloorsSeatMap.set(
            this.currentSelectedFloor,
            this.eachFloorSeatMap.get(this.currentSelectedFloor)
          );
          console.log(
            "eachFloorSeatMap >>>>>" +
              this.eachFloorSeatMap.get(this.currentSelectedFloor)
          );
          console.log(
            "allFloorsSeatMap >>>>>" +
              this.allFloorsSeatMap.get(this.currentSelectedFloor)
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
      var mynewSeatList = this.getEachFloorSeatList(this.currentSelectedFloor);

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

      this.floorCanvas.add(group);

      mynewSeatList.push(group);

      this.allFloorsSeatMap.set(
        this.currentSelectedFloor,
        this.eachFloorSeatMap.get(this.currentSelectedFloor)
      );
      console.log("allFloorsSeatMap-size : " + this.allFloorsSeatMap.size);

      console.log(this.allFloorsSeatMap.get(this.currentSelectedFloor));
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