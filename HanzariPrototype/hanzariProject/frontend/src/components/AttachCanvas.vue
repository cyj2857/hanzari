<template>
  <div>
    <canvas
      ref="canvas"
      class="canvas"
      width="950px"
      height="800px"
      style="text-align: center"
    ></canvas>
    <input
      v-show="false"
      ref="inputUpload"
      type="file"
      @change="onFileChange"
    />
    <v-btn color="success" @click="$refs.inputUpload.click()"
      >File Upload to Background</v-btn
    >
    <v-btn @click="addVacantBtn" color="primary" dark>Add Vacant</v-btn>
    <v-btn @click="deleteBtn">Delete Selected Shape</v-btn>
    <v-btn @click="deleteAllBtn">Delete All Shapes</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
    <v-btn @click="clickLoadBtn">Load Canvas</v-btn>
    <v-btn @click="clickChangeToVacant">Change to Vacant</v-btn>
    <EmployeeDialog :dialogStatus="this.dialogStatus" @close="closeDialog" />
  </div>
</template>

<script>
import axios from "axios";
import { eventBus } from "../main.js";
import EmployeeDialog from "@/components/EmployeeDialog.vue";
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";
const portNum = 8080;
const host = "172.30.1.50";
export default {
  props: ["seat", "employee"],
  components: {
    EmployeeDialog,
    AllFloorsDataTable,
  },
  data: function () {
    return {
      floorCanvas: null,
      floorImageList: null,
      seatId: 0,
      currentSelectedFloor: null,
      eachFloorSeatMap: null, //current floor's seat map
      eachEmployeeSeatMap: null, //each Employee's seats map
      dialogStatus: false,
      menuStatus: false,
      allEmployeeList: [],
      seats: this.seat,
      employees: this.employee,
    };
  },
  created() {
    eventBus.$on("createSeat", (item) => {
      this.createSeat(item);
    }),
      eventBus.$on("showSeat", (seat) => {
        this.showSeat(seat);
      }),
      eventBus.$on("changeFloor", (floor) => {
        this.currentSelectedFloor = floor;
        this.changeFloor(this.currentSelectedFloor);
        console.log(this.currentSelectedFloor + "여기가 현재층");
      }),
      eventBus.$on("allEmployeeList", (allEmployeeList) => {
        this.allEmployeeList = allEmployeeList;
      });
    eventBus.$on("MappingSeat", (item) => {
      this.setVacantSeat(item);
    });
    if (this.floorImageList == null) {
      this.floorImageList = new Map();
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
  computed: {
    menuInVisible() {
      return this.menuStatus;
    },
  },
  methods: {
    setMenuPosition(x, y) {
      console.log(x, y);
      this.openMenu();
    },
    openMenu() {
      this.menuStatus = true;
    },
    closeMenu() {
      this.menuStatus = false;
    },
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
        this.floorCanvas = new fabric.Canvas(ref, {
          fireRightClick: true, // <-- enable firing of right click events
          fireMiddleClick: true, // <-- enable firing of middle click events
          stopContextMenu: true, // <--  prevent context menu from showing
        });
      }
    },
    changeFloor(floor) {
      //도형 랜더링 전에 화면의 도형들을  초기화
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach((obj) => {
          this.floorCanvas.remove(obj);
        });

      //각 층의 저장된 도형 리스트 화면에 뿌려주기
      //현재 층의 이미지가 저장되어있다면
      let myOnefloorSeatList = this.getEachFloorSeatList(floor);

      if (this.floorImageList.get(floor) != null) {
        this.loadImage(this.floorImageList.get(floor));

        //현재 층에 그린 도형들이 있다면
        if (myOnefloorSeatList) {
          for (let i = 0; i < myOnefloorSeatList.length; i++) {
            this.floorCanvas.add(myOnefloorSeatList[i]);
            console.log("myOnefloorSeatList : ");
            console.log(myOnefloorSeatList[i]);
          }

          eventBus.$emit("eachFloorSeatList", myOnefloorSeatList);
        }
      } else if (this.floorImageList.get(floor) == null) {
        //현재 층의 이미지가 저장되어있지 않다면
        //화면에 그려져있던 이미지와 도형 초기화
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            this.floorCanvas.remove(obj);
          });

        this.floorCanvas.backgroundImage = 0;
        this.floorCanvas.backgroundColor = "aliceblue";
        this.floorCanvas.renderAll();

        eventBus.$emit("eachFloorSeatList", myOnefloorSeatList);
      }
    },
    createImage(file) {
      this.loadImage(file);
      this.saveImage(file);
    },
    loadImage(file) {
      let reader = new FileReader();
      reader.onload = (e) => {
        fabric.Image.fromURL(e.target.result, (img) => {
          img.set({
            scaleX: this.floorCanvas.width / img.width,
            scaleY: this.floorCanvas.height / img.height,
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
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
    },
    //도형생성시
    createSeat(item) {
      if (!this.floorImageList.get(this.currentSelectedFloor)) {
        alert("도면 이미지가 없습니다");
        console.log(this.getEachFloorSeatList(this.currentSelectedFloor));
        return;
      }
      console.log("currnet floor is " + this.currentSelectedFloor);

      //각 층에 해당하는 도형 리스트 리턴하기
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );
      //각 사원의 자리 리스트 리턴하기
      let eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);
      let rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: this.getColor(item.department),
        opacity: 1,
      });
      let textObject = new fabric.IText(item.name, {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 13,
        fill: "black",
      });
      let group = new fabric.Group([rectangle, textObject], {
        seatId: this.currentSelectedFloor + "-" + this.seatId++, // currentSelectedFloor-seatId
        employee_name: item.name,
        employee_department: item.department,
        employee_number: item.number,
        employee_id: item.employee_id,
        floor_id: this.currentSelectedFloor,
        left: 150,
        top: 150,
      });
      group.on("mousedown", (e) => {
        let group = e.target;
        if (e.button === 1) {
          console.log("left click");
          // 삭제될 자리 seatId 보내고
          // 그 seatId에 있는 employee null
        }
        if (e.button === 2) {
          //console.log("middle click");
        }
        if (e.button === 3) {
          //console.log("right click");
          //context menu 넣을 곳
        }
      });
      group.on("mousedblclick", (e) => {
        let group = e.target;
        let groupToObject = group.toObject([
          "employee_id",
          "employee_name",
          "floor_id",
          "employee_department",
        ]);
        eventBus.$emit("employee_id", groupToObject.employee_id);
        eventBus.$emit("employee_name", groupToObject.employee_name);
        eventBus.$emit("floor_id", groupToObject.floor_id);
        eventBus.$emit(
          "employee_department",
          groupToObject.employee_department
        );
        this.getDialog();
      });
      this.floorCanvas.add(group);
      eachFloorSeatList.push(group);
      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);

      let groupToObject = group.toObject(["seatId"]);
      eachEmployeeSeatList.push(groupToObject.seatId);

      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    getColor(department) {
      const Colors = {
        Orange: "orange",
        Yellow: "yellow",
        Green: "green",
        Blue: "blue",
        Gray: "Gray",
      };
      if (department == "부서1") return Colors.Orange;
      else if (department == "부서2") return Colors.Yellow;
      else if (department == "부서3") return Colors.Green;
      else return Colors.Gray;
    },
    showSeat(seat) {
      console.log(seat.seat_id + "가 해당 자리의 아이디입니다."); // One-0
      let seatFloor;

      //seat의 층과 현재층이 같지 않다면
      if (this.currentSelectedFloor != seat.seat_id.split("-")[0]) {
        //탭 전환 코드
        seatFloor = seat.seat_id.split("-")[0];
      }
      //seat의 층과 현재층이 같다면
      else {
        seatFloor = this.currentSelectedFloor;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(seatFloor);
      for (let i = 0; i < eachFloorSeatList.length; i++) {
        let myGroup = eachFloorSeatList[i];
        let asObject = myGroup.toObject([
          "employee_id",
          "floor_id",
          "seatId",
          "employee_department",
        ]);
        console.log(asObject.floor_id + "층에 자리가 있습니다.");

        let objectSeatId = asObject.seatId + "번";
        if (seat.seat_id == objectSeatId) {
          console.log("hihi");
          this.floorCanvas
            .getObjects()
            .slice()
            .forEach((obj) => {
              this.floorCanvas.remove(obj);
            });

          //각 층의 저장된 도형 리스트 화면에 뿌려주기
          //현재 층의 이미지가 저장되어있다면
          if (this.floorImageList.get(seatFloor) != null) {
            this.loadImage(this.floorImageList.get(seatFloor));

            for (let i = 0; i < eachFloorSeatList.length; i++) {
              this.floorCanvas.add(eachFloorSeatList[i]);
            }
          }
          // myGroup.item(0).set("fill", "red");
          myGroup.item(0).animate("fill", "red", {
            onChange: this.floorCanvas.renderAll.bind(this.floorCanvas),
            duration: 2000,
            onComplete: orgincolor,
          });
          let color = this.getColor(asObject.employee_department);
          function orgincolor() {
            console.log(color);
            myGroup.item(0).set("fill", color);
          }
        }
        //자리가 아직 없을때 예외처리 하기
      }
    },
    //각 층의 도형 리스트 반환하기
    getEachFloorSeatList: function (floor) {
      //층에 해당하는 도형리스트가 만들어지지 않았을때 각 층의 도형 리스트 생성하기
      if (!this.eachFloorSeatMap.get(floor)) {
        let newSeatsList = new Array();
        this.eachFloorSeatMap.set(floor, newSeatsList);
        console.log(this.eachFloorSeatMap.size + "처음의 자리 맵 사이즈입니다");
        return this.eachFloorSeatMap.get(floor);
      } else {
        console.log(this.eachFloorSeatMap.size + "현재 자리 맵 사이즈입니다");
        return this.eachFloorSeatMap.get(floor);
      }
    },

    //각 사원의 도형 리스트 반환하기
    getEachEmployeeSeatList: function (employee_id) {
      if (!this.eachEmployeeSeatMap.get(employee_id)) {
        let newEmployeeSeatList = new Array();
        this.eachEmployeeSeatMap.set(employee_id, newEmployeeSeatList);
        return this.eachEmployeeSeatMap.get(employee_id);
      } else {
        return this.eachEmployeeSeatMap.get(employee_id);
      }
    },
    //해당 층의 도형 리스트 삭제하기
    deleteEachFloorSeatList: function (floor) {
      this.getEachFloorSeatList(floor).length = 0;
      return this.getEachFloorSeatList(floor);
    },
    //자리비우기
    clickChangeToVacant() {
      let activeObject = null;
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      if (this.floorCanvas.getActiveObject().type !== "group") {
        console.log("!!!not group!!!");
        return;
      } else {
        console.log("!!!group!!!!");
        activeObject = this.floorCanvas.getActiveObject(); // 선택 객체 가져오기

        activeObject.employee_name = null;
        activeObject.employee_department = null;
        activeObject.employee_number = null;
        activeObject.employee_id = null; // delete employee information in group

        activeObject
          .item(0)
          .set("fill", this.getColor(activeObject.employee_department));
        this.floorCanvas.remove(activeObject.item(1)); // delete textObject

        activeObject._objects[1].text = "";
        //activeObject.item(1).text = null
        //this.floorCanvas.requestRenderAll();
        this.floorCanvas.renderAll();
        //console.log(eachFloorSeatList);
      }
      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
    },
    deleteAllBtn() {
      if (confirm("Are you sure?")) {
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            this.floorCanvas.remove(obj);
          });

        let eachFloorSeatList = this.deleteEachFloorSeatList(
          this.currentSelectedFloor
        );
        if (eachFloorSeatList) {
          alert("success");
        } else alert("fail");

        eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      }
    },

    deleteBtn() {
      //좌석 지우면 list에 있는거 없애기
      let activeObject = null;
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );
      let shapearray = new Array();

      if (this.floorCanvas.getActiveObjects().length == 1) {
        activeObject = this.floorCanvas.getActiveObject();
        console.log("단일객체 선택");
      } else {
        activeObject = this.floorCanvas.getActiveObject().toGroup();
        console.log("복수객체 선택");
      }

      this.floorCanvas
        .getObjects()
        .slice()
        .forEach((obj) => {
          shapearray.push(obj);
        });

      if (activeObject) {
        if (confirm("Are you sure?")) {
          shapearray.slice().forEach((obj) => {
            if (obj == activeObject) {
              //delete
              let index = shapearray.indexOf(activeObject);
              shapearray.splice(index, 1);
            }
          });
          this.floorCanvas.remove(activeObject);
          //modify map(eachFloorSeatMap)
          eachFloorSeatList.length = 0;
          this.eachFloorSeatMap.set(this.currentSelectedFloor, shapearray);

          eventBus.$emit(
            "eachFloorSeatList",
            this.getEachFloorSeatList(this.currentSelectedFloor)
          );
        }
      }
    },

    addVacantBtn(item) {
      //각 층에 해당하는 도형 리스트 리턴하기
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      console.log("currnet floor is " + this.currentSelectedFloor);

      let rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: this.getColor(item.department),
        opacity: 1,
      });

      let textObject = new fabric.IText("", {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 13,
        fill: "black",
      });

      let group = new fabric.Group([rectangle, textObject], {
        floor_id: this.currentSelectedFloor,
        seatId: this.currentSelectedFloor + "-" + this.seatId++, // currentSelectedFloor-seatId
        employee_name: item.name,
        employee_department: item.department,
        employee_number: item.number,
        employee_id: item.employee_id,
        left: 150,
        top: 150,
      });

      group.on("mouseover", function (e) {
        let group = e.target;
        let asObject = group.toObject(["seatId"]);
        let x = group.toObject(["left"]);

        console.log("seatId = " + asObject.seatId);
        console.log("left = " + x.left); //150
      });

      group.on("mousedblclick", (e) => {
        let group = e.target;
        let groupToObject = group.toObject([
          "employee_id",
          "employee_name",
          "floor_id",
          "employee_department",
        ]);
        eventBus.$emit("employee_id", groupToObject.employee_id);
        eventBus.$emit("employee_name", groupToObject.employee_name);
        eventBus.$emit("floor_id", groupToObject.floor_id);
        eventBus.$emit(
          "employee_department",
          groupToObject.employee_department
        );
        this.getDialog();
      });

      this.floorCanvas.add(group);
      eachFloorSeatList.push(group);

      console.log("공석 이름 : " + item.name);
      console.log("전체층의 자리 맵 size = " + this.eachFloorSeatMap.size);
      console.log(
        this.currentSelectedFloor +
          "의 자리 리스트 length = " +
          eachFloorSeatList.length
      );

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
    },

    setVacantSeat(item) {
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      let activeObject = this.floorCanvas.getActiveObject();
      (activeObject.employee_name = item.name),
        (activeObject.employee_department = item.department),
        (activeObject.employee_number = item.number),
        (activeObject.employee_id = item.employee_id);

      activeObject
        .item(0)
        .set("fill", this.getColor(activeObject.employee_department));
      activeObject._objects[1].text = item.name;
      this.floorCanvas.renderAll();

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      //여기
    },
    makeGroupInfo(seat, employee) {
      let rectangle = new fabric.Rect({
        width: seat.width,
        height: seat.height,
        fill: this.getColor(employee.department),
      });
      let textObject = new fabric.IText(employee.name, {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 13,
        fill: "black",
      });
      let group = new fabric.Group([rectangle, textObject], {
        seatId: seat.seat_id,
        employee_name: employee.name,
        employee_department: employee.department,
        employee_number: employee.number,
        employee_id: seat.employee_id,
        floor_id: seat.floor, //One이라고 가정
        left: seat.x,
        top: seat.y,
      });
      group.on("mousedblclick", (e) => {
        let group = e.target;
        let groupToObject = group.toObject([
          "employee_id",
          "employee_name",
          "floor_id",
          "employee_department",
        ]);
        eventBus.$emit("employee_id", groupToObject.employee_id);
        eventBus.$emit("employee_name", groupToObject.employee_name);
        eventBus.$emit("floor_id", groupToObject.floor_id);
        eventBus.$emit(
          "employee_department",
          groupToObject.employee_department
        );
        this.getDialog();
      });
      return group;
    },
    /*!!!!!!!!!!!!!!!axios 관련 코드 app.vue에 다 옮길 예정!!!!!!!!!!!!!!!
    seat VM , employee VM 만 보고 view(component) 다루기위함 */

    //아직 구현중에 있습니다.
    clickSaveBtn() {
      //일단 현재 층에 대한 정보만 저장하는 방식으로 코드를 구현 //추후에 상위 Map을 저장 시킬 예정임.
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );
      console.log(eachFloorSeatList.length + "현재 층의 자리 개수입니다.");

      if (eachFloorSeatList) {
        for (var i = 0; i < eachFloorSeatList.length; i++) {
          let groupToObject = eachFloorSeatList[i].toObject([
            "seatId",
            "floor_id",
            "left",
            "top",
            "employee_department",
            "employee_id",
          ]);

          let data = {};
          data.seat_id = groupToObject.seatId;
          data.floor = groupToObject.floor_id;
          data.x = groupToObject.left;
          data.y = groupToObject.top;
          data.is_group = false;
          data.group_id = null;
          data.building_id = "HANCOM01";
          data.employee_id = groupToObject.employee_id;
          data.width = 50.5;
          data.height = 50.5;
          data.degree = 0;
          data.shape_id = "1";

          this.saveAllSeatByAxios(data);
        }
      }
    },
    saveAllSeatByAxios(data) {
      axios
        .post(
          "http://" + host + ":" + portNum + "/seats",
          JSON.stringify(data),
          {
            headers: { "Content-Type": `application/json` },
          }
        )
        .then((res) => {
          console.log(res.data);
        });
    },
    clickLoadBtn() {
      /*이후에 내부에 있는 중복 로직은 함수로 뺄 예정 (rectangle, textObject, grouping 과정 및 group의 interaction ) */
      let eachFloorSeatList = null;
      let eachEmployeeSeatList = null;

      let group = null;

      for (let i = 0; i < this.seats.length; i++) {
        for (let j = 0; j < this.employees.length; j++) {
          //현재 층 list 다루기
          if (
            this.seats[i].floor == this.currentSelectedFloor &&
            this.employees[j].employee_id == this.seats[i].employee_id
          ) {
            eachFloorSeatList = this.getEachFloorSeatList(
              this.currentSelectedFloor
            );

            eachEmployeeSeatList = this.getEachEmployeeSeatList(
              this.seats[i].employee_id
            );

            group = this.makeGroupInfo(this.seats[i], this.employee[j]);

            this.floorCanvas.add(group);
            eachFloorSeatList.push(group);
            console.log(eachFloorSeatList);
            eventBus.$emit("eachFloorSeatList", eachFloorSeatList);

            let groupToObject = group.toObject(["seatId"]);
            eachEmployeeSeatList.push(groupToObject.seatId);

            eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
            console.log(
              this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
            );
            console.log(
              this.seats[i].employee_id +
                "의 자리 리스트 개수는 " +
                this.getEachEmployeeSeatList(this.seats[i].employee_id).length +
                "입니다."
            );
          } //end of if

          //다른 층 eachFloorSeatList에 넣기
          else if (
            this.seat[i].floor != this.currentSelectedFloor &&
            this.employees[j].employee_id == this.seats[i].employee_id
          ) {
            eachFloorSeatList = this.getEachFloorSeatList(this.seat[i].floor);
            eachEmployeeSeatList = this.getEachEmployeeSeatList(
              this.seats[i].employee_id
            );

            group = this.makeGroupInfo(this.seats[i], this.employee[j]);

            eachFloorSeatList.push(group);

            eventBus.$emit("eachFloorSeatList", eachFloorSeatList);

            let groupToObject = group.toObject(["seatId"]);
            eachEmployeeSeatList.push(groupToObject.seatId);

            eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
            console.log(
              this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
            );
            console.log(
              this.seats[i].employee_id +
                "의 자리 리스트 개수는 " +
                this.getEachEmployeeSeatList(this.seats[i].employee_id).length +
                "입니다."
            );
          } //end of else if
        }
      }
    },
  },
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
ul {
  padding: 0px;
  margin: 0px;
  display: block;
}
</style>
