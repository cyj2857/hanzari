<template>
  <div>
    <canvas ref="canvas" class="canvas" width="1100px" height="800px"></canvas>
    <input v-show="false" ref="inputUpload" type="file" @change="onFileChange" />
    <v-btn color="success" @click="$refs.inputUpload.click()">File Upload to Background</v-btn>
    <v-btn @click="addVacantBtn" color="primary" dark>Add Vacant</v-btn>
    <v-btn @click="deleteBtn">Delete Selected Shape</v-btn>
    <v-btn @click="deleteAllBtn">Delete All Shapes</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
    <EmployeeDialog :dialogStatus="this.dialogStatus" @close="closeDialog" />
    <v-btn @click="getDialog">Show Seat Info</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import axios from "axios";
import EmployeeDialog from "@/components/EmployeeDialog.vue";

export default {
  components: {
    EmployeeDialog
  },
  data: function() {
    return {
      floorCanvas: null,
      floorImageList: null,
      seatId: 0,
      currentSelectedFloor: null,
      eachFloorSeatMap: null, //current floor's seat map
      allFloorsSeatMap: null, //all floor's seat map
      eachEmployeeSeatMap: null, //each Employee's seats map
      dialogStatus: false
    };
  },
  created() {
    eventBus.$on("createSeat", item => {
      this.createSeat(item);
    }),
      eventBus.$on("showSeat", seat => {
        this.showSeat(seat);
      }),
      eventBus.$on("changeFloor", floor => {
        this.currentSelectedFloor = floor;
        this.changeFloor(this.currentSelectedFloor);
        console.log(this.currentSelectedFloor + "여기가 현재층");
      });
    if (this.floorImageList == null) {
      this.floorImageList = new Map();
    }
    if (this.allFloorsSeatMap == null) {
      this.allFloorsSeatMap = new Map();
    }
    if (this.eachFloorSeatMap == null) {
      this.eachFloorSeatMap = new Map();
    }
    if (this.eachEmployeeSeatMap == null) {
      this.eachEmployeeSeatMap = new Map();
    }
  },
  mounted() {
    this.initializing();
  },
  methods: {
    getDialog() {
      this.dialogStatus = true;
      console.log(this.dialogStatus);
    },
    closeDialog() {
      console.log("<<<close dialog>>>");
      this.dialogStatus = false;
      console.log(this.dialogStatus);
    },
    //canvas, map 생성
    initializing() {
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        this.floorCanvas = new fabric.Canvas(ref);
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
            console.log("myOnefloorSeatList : ");
            console.log(myOnefloorSeatList[i]);
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
      eventBus.$emit("eachFloorSeatList", myOnefloorSeatList);
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
      console.log("floorImageList : ");
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

      //각 사원의 자리 리스트 리턴하기
      var eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);

      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: this.getColor(item.department),
        opacity: 1
      });
      var textObject = new fabric.IText(item.name, {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 13,
        fill: "black"
      });

      var group = new fabric.Group([rectangle, textObject], {
        id: item.employee_id,
        seatId: this.currentSelectedFloor + "-" + this.seatId++, // 1,2,3,4
        employee_name: item.name,
        employee_department: item.department,
        employee_number: item.number,
        employee_id: item.employee_id,
        floor_id: this.currentSelectedFloor,
        left: 150,
        top: 150
      });
      group.on("mouseover", function(e) {
        var group = e.target;
        var asObject = group.toObject(["employee_id"]);
        var x = group.toObject(["left"]);
        console.log("employee id = " + asObject.employee_id); //1771354
        //console.log(asObject.floor_id+"층에 자리가 생성되었습니다.");
        console.log("left = " + x.left); //150
      });
      group.on("mousedown", function(e) {
        var group = e.target;
        eventBus.$emit(
          "employee_id",
          group.toObject(["employee_id"]).employee_id
        );
        eventBus.$emit(
          "employee_name",
          group.toObject(["employee_name"]).employee_name
        );
        eventBus.$emit("floor_id", group.toObject(["floor_id"]).floor_id);
      });

      this.floorCanvas.add(group);
      eachFloorSeatList.push(group);

      var groupToObject = group.toObject(["seatId"]);
      eachEmployeeSeatList.push(groupToObject.seatId);
      console.log(
        item.name + "의 자리의 개수는 " + eachEmployeeSeatList.length + "입니다"
      );

      // var asObject = group.toObject(["seatId"]);
      // console.log(asObject.seatId);
      // this.floorCanvas.add(group);

      //각 층의 도형 리스트에 하나의 해당 도형을 넣기
      //eachFloorSeatList.push(group);
      //각 층의 도형 리스트를 접근 할 수 있는 map에 도형리스트를 저장하기
      //실질적으로 allFloorsSeatMap로 각 층의 도형 리스트를 접근한다

      this.allFloorsSeatMap.set(
        this.currentSelectedFloor,
        this.eachFloorSeatMap.get(this.currentSelectedFloor)
      );
      console.log("allFloorsSeatMap-size : " + this.allFloorsSeatMap.size);

      console.log("allFloorsSeatMap : ");
      console.log(this.allFloorsSeatMap.get(this.currentSelectedFloor));

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      /////////////////////////////////////////////
      // this.eachEmployeeSeatMap.set(
      //   item.employee_id, eachEmployeeSeatList
      // )
      console.log("eachEmployeeSeatMap-size:" + this.eachEmployeeSeatMap.size);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
      ////////////////////////////
    },
    getColor(department) {
      const Colors = {
        Orange: "orange",
        Yellow: "yellow",
        Green: "green",
        Blue: "blue"
      };
      if (department == "Development Team") return Colors.Orange;
      else if (department == "Secure Team") return Colors.Yellow;
      else if (department == "Marketing Team") return Colors.Green;
      else return Colors.Blue;
    },
    showSeat(seat) {
      //seat의 층과 현재층이 같지 않다면
      console.log(seat.seat_id + "가 해당 자리의 아이디입니다.");
      var seatFloor;

      if (this.currentSelectedFloor != seat.seat_id.split("-")[0]) {
        //탭 전환 코드
        seatFloor = seat.seat_id.split("-")[0];
      }
      //seat의 층과 현재층이 같다면
      else {
        seatFloor = this.currentSelectedFloor;
      }
      var eachFloorSeatList = this.getEachFloorSeatList(seatFloor);

      for (var i = 0; i < eachFloorSeatList.length; i++) {
        var myGroup = eachFloorSeatList[i];
        var asObject = myGroup.toObject(["employee_id", "floor_id", "seatId"]);
        console.log(asObject.floor_id + "층에 자리가 있습니다.");

        var objectSeatId = asObject.seatId + "번";
        if (seat.seat_id == objectSeatId) {
          console.log("hihi");
          this.floorCanvas
            .getObjects()
            .slice()
            .forEach(obj => {
              this.floorCanvas.remove(obj);
            });

          //각 층의 저장된 도형 리스트 화면에 뿌려주기
          //현재 층의 이미지가 저장되어있다면
          if (this.floorImageList.get(seatFloor) != null) {
            this.loadImage(this.floorImageList.get(seatFloor));

            for (var i = 0; i < eachFloorSeatList.length; i++) {
              this.floorCanvas.add(eachFloorSeatList[i]);
            }
          }
          myGroup.item(0).set("fill", "red");
        }
        //자리가 아직 없을때 예외처리 하기
      }
    },
    //각 층의 도형 리스트 생성하기
    getEachFloorSeatList: function(floor) {
      //층에 해당하는 도형리스트가 만들어지지 않았을때
      if (!this.eachFloorSeatMap.get(floor)) {
        var newSeatsList = new Array();
        this.eachFloorSeatMap.set(floor, newSeatsList);
        return this.eachFloorSeatMap.get(floor);
      } else {
        return this.eachFloorSeatMap.get(floor);
      }
    },

    getEachEmployeeSeatList: function(employee_id) {
      if (!this.eachEmployeeSeatMap.get(employee_id)) {
        var newEmployeeSeatList = new Array();
        this.eachEmployeeSeatMap.set(employee_id, newEmployeeSeatList);
        return this.eachEmployeeSeatMap.get(employee_id);
      } else {
        return this.eachEmployeeSeatMap.get(employee_id);
      }
    },
    deleteAllBtn() {
      //그 층의 모든 list 없애기
      if (confirm("Are you sure?")) {
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
      }
    },
    deleteBtn() {
      //좌석 지우면 list에 있는거 없애기
      var activeObject = null;
      if (this.floorCanvas.getActiveObjects().length == 1) {
        activeObject = this.floorCanvas.getActiveObject();
        console.log("단일객체 선택");
      } else {
        activeObject = this.floorCanvas.getActiveObject().toGroup();
        console.log("복수객체 선택");
      }

      var shapearray = new Array();
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          shapearray.push(obj);
        });
      if (activeObject) {
        if (confirm("Are you sure?")) {
          shapearray.slice().forEach(obj => {
            if (obj == activeObject) {
              //delete
              var index = shapearray.indexOf(activeObject);
              shapearray.splice(index, 1);
            }
          });
          this.floorCanvas.remove(activeObject);
          //modify map(eachFloorSeatMap)
          this.eachFloorSeatMap.get(this.currentSelectedFloor).length = 0;
          this.allFloorsSeatMap.delete(this.currentSelectedFloor);
          this.eachFloorSeatMap.set(this.currentSelectedFloor, shapearray);
          this.allFloorsSeatMap.set(
            this.currentSelectedFloor,
            this.eachFloorSeatMap.get(this.currentSelectedFloor)
          );
        }
      }
    },
    changecolorBtn() {
      var activeObject = this.floorCanvas.getActiveObject();
      var eachfloor = this.eachFloorSeatMap.get(this.currentSelectedFloor);

      if (activeObject) {
        eachfloor.slice().forEach(obj => {
          if (obj == activeObject) {
            //modify color
            obj.item(0).set("fill", "black");
          }
        });
        this.floorCanvas.renderAll();
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
        fill: "gray",
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

        console.log("seatId = " + asObject.seatId);
        console.log("left = " + x.left); //150
      });
      group.on("mousedown", function(e) {
        var group = e.target;
        eventBus.$emit("employee_id", null);
        eventBus.$emit("employee_name", null);
        eventBus.$emit("floor_id", null);
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
