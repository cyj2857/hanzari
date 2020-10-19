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
    <v-menu>
      <template v-slot:activator="{ on, attrs }"
        ><v-btn v-bind="attrs" v-on="on">Multiple</v-btn></template
      >
      <v-list>
        <v-list-item
          @click="multipleVacant(item.number)"
          v-for="(item, index) in items"
          :key="index"
        >
          <v-list-item-title>{{ item.number }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-btn @click="addVacantBtn" color="primary" dark>Add Vacant</v-btn>
    <v-btn @click="deleteBtn">Delete Selected Shape</v-btn>
    <v-btn @click="deleteAllBtn">Delete All Shapes</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
    <v-btn @click="clickLoadBtn">Load Canvas</v-btn>
    <v-btn @click="clickChangeToVacant">Change to Vacant</v-btn>
    <EmployeeDialog
      :dialogStatus="this.employeeDialogStatus"
      @close="closeEmployeeDialog"
    />
    <ChangeSeatDialog
      :dialogStatus="this.changeSeatDialogStatus"
      @close="closeChangeSeatDialog"
    />
  </div>
</template>

<script>
import axios from "axios";
import { eventBus } from "../main.js";
import EmployeeDialog from "@/components/EmployeeDialog.vue";
import ChangeSeatDialog from "@/components/ChangeSeatDialog.vue";
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";
const portNum = 8081;
const host = "172.30.1.50";
export default {
  props: ["seat", "employee"],
  components: {
    EmployeeDialog,
    AllFloorsDataTable,
    ChangeSeatDialog,
  },
  data: function () {
    return {
      floorCanvas: null,
      floorImageList: null,
      seatId: null,
      currentSelectedFloor: null,
      eachFloorSeatMap: null, //current floor's seat map
      eachEmployeeSeatMap: null, //each Employee's seats map
      employeeDialogStatus: false,
      changeSeatDialogStatus: false,
      inputChangeSeatFloor: null,
      inputChangeSeatX: null,
      inputChangeSeatY: null,
      allEmployeeList: [],
      seats: this.seat,
      employees: this.employee,
      items: [{ number: 2 }, { number: 4 }, { number: 6 }, { number: 8 }],
      allFloorItems: [],
    };
  },
  created() {
    eventBus.$on("confirmChangeSeatDialog", (inputInfo) => {
      this.confirmChangeSeatDialog(inputInfo);
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
    eventBus.$on("allFloorItems", (allItems) => {
      //to save floor information
      this.allFloorItems = allItems;
      console.log("in AttachCanvas");
      console.log(this.allFloorItems); // floor_name 뽑아내야함
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
  methods: {
    getEmployeeDialog() {
      this.employeeDialogStatus = true;
      console.log(this.employeeDialogStatus);
    },
    closeEmployeeDialog() {
      console.log("<<<close dialog>>>");
      this.employeeDialogStatus = false;
      console.log(this.employeeDialogStatus);
    },
    getChangeSeatDialog() {
      eventBus.$emit("initChangeSeatDialog", null);
      this.changeSeatDialogStatus = true;
      console.log(this.changeSeatDialogStatus);
    },
    closeChangeSeatDialog() {
      console.log("<<<close dialog>>>");
      this.changeSeatDialogStatus = false;
      console.log(this.changeSeatDialogStatus);
    },
    // 층간이동
    confirmChangeSeatDialog(inputInfo) {
      console.log("<<<confirm dialog>>>");

      this.changeSeatDialogStatus = false;

      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      let activeObject = this.floorCanvas.getActiveObject();

      activeObject.floor_id = inputInfo[0];
      activeObject.left = parseInt(inputInfo[1]);
      activeObject.top = parseInt(inputInfo[2]);

      for (let i = 0; i < eachFloorSeatList.length; i++) {
        if (eachFloorSeatList[i].seatId == activeObject.seatId) {
          eachFloorSeatList.splice(i, 1);
        }
      }

      console.log("after delete");
      console.log(eachFloorSeatList.length);

      let changeFloorSeatList = this.getEachFloorSeatList(
        inputInfo[0] //input floor
      );
      changeFloorSeatList.push(activeObject);

      eventBus.$emit("showSeatFloor", inputInfo[0]);
      eventBus.$emit("eachFloorSeatList", changeFloorSeatList);
      this.floorCanvas.renderAll();
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
        this.floorCanvas.on("mouse:wheel", (opt) => {
          if (!this.floorCanvas.viewportTransform) {
            return;
          }
          let evt = opt.e;
          if (evt.ctrlKey === true) {
            let evt = opt.e;
            let deltaY = evt.deltaY;
            let zoom = this.floorCanvas.getZoom();
            zoom = zoom - deltaY / 300;
            if (zoom > 20) zoom = 20;
            if (zoom < 1) zoom = 0.95;
            this.floorCanvas.zoomToPoint(
              new fabric.Point(evt.offsetX, evt.offsetY),
              zoom
            );
          } /*else {//scroll event
            this.floorCanvas.viewportTransform[4] += (evt.deltaX)*-1; 
            this.floorCanvas.viewportTransform[5] += (evt.deltaY)*-1;
            this.floorCanvas.requestRenderAll();
            this.setState({ lastPosX: evt.clientX });
            this.setState({ lastPosY: evt.clientY });
          }*/
          opt.e.preventDefault();
          opt.e.stopPropagation();
        });
      }
    },
    getSeatUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    multipleVacant(number) {
      this.addVacantBtn(number);
      // for (let i=1; i<=number; i++){
      //this.addVacantBtn();
      // }
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
      if (this.currentSelectedFloor != seat.floor_id) {
        //탭 전환 코드
        seatFloor = seat.floor_id;
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
    //자리비우기(allFloorSeatMap에서 seatId 정보는 뺴지 않고 그대로 두기, 사원의 자리 리스트에서만 해당 seatId 제거하기)
    clickChangeToVacant() {
      let activeObject = null;
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      if (!this.floorCanvas.getActiveObject()) {
        return;
      }
      if (this.floorCanvas.getActiveObject().type == "group") {
        console.log("!!!group!!!!");
        activeObject = this.floorCanvas.getActiveObject(); // 선택 객체 가져오기

        activeObject.employee_name = null;
        activeObject.employee_department = null;
        activeObject.employee_number = null;

        let groupToObject = activeObject.toObject(["seatId", "employee_id"]);
        console.log(groupToObject.seatId + "비우고자하는 자리의 SeatId입니다.");
        console.log(
          groupToObject.employee_id + "비우고자하는 자리의 employee_id입니다."
        );
        this.deleteEachEmployeeSeatList(groupToObject);

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
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    //해당 층의 도형 리스트 전체 삭제하기
    deleteEachFloorSeatList: function (floor) {
      this.getEachFloorSeatList(floor).length = 0;
      return this.getEachFloorSeatList(floor);
    },
    //사원의 자리리스트에서 삭제된 자리를 삭제하기
    deleteEachEmployeeSeatList: function (groupToObject) {
      let oneEmployeeSeatList = this.getEachEmployeeSeatList(
        groupToObject.employee_id
      );

      for (let i = 0; i < oneEmployeeSeatList.length; i++) {
        if (oneEmployeeSeatList[i] == groupToObject.seatId) {
          oneEmployeeSeatList.splice(i, 1);
        }
      }
      console.log(
        oneEmployeeSeatList.length + "전체 삭제한 자리 리스트 길이입니다."
      );
    },
    deleteAllBtn() {
      if (confirm("Are you sure?")) {
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            this.floorCanvas.remove(obj);

            let groupToObject = obj.toObject(["seatId", "employee_id"]);

            this.deleteEachEmployeeSeatList(groupToObject);
          });

        let eachFloorSeatList = this.deleteEachFloorSeatList(
          this.currentSelectedFloor
        );

        if (eachFloorSeatList) {
          alert("success");
        } else alert("fail");

        eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
        eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
      }
    },

    deleteBtn() {
      //좌석 지우면 list에 있는거 없애기
      let activeObject = null;
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );
      let shapearray = new Array();

      if (confirm("Are you sure?")) {
        if (this.floorCanvas.getActiveObjects().length == 1) {
          activeObject = this.floorCanvas.getActiveObject();
          console.log("단일객체 선택");

          let groupToObject = activeObject.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
        } else {
          activeObject = this.floorCanvas.getActiveObjects();
          console.log("복수객체 선택");

          for (let i = 0; i < activeObject.length; i++) {
            let groupToObject = activeObject[i].toObject([
              "seatId",
              "employee_id",
            ]);
            this.deleteEachEmployeeSeatList(groupToObject);
          }

          activeObject = this.floorCanvas.getActiveObject().toGroup();
        }

        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            shapearray.push(obj);
          });

        if (activeObject) {
          //층의 자리 리스트에서 제거하기
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
          eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
        }
      }
    },

    addVacantBtn(number) {
      let VacantPositon = [
        { left: 50, top: 100 },
        { left: 125, top: 100 },
        { left: 50, top: 200 },
        { left: 125, top: 200 },
        { left: 200, top: 100 },
        { left: 200, top: 200 },
        { left: 275, top: 100 },
        { left: 275, top: 200 },
      ];

      let vacantnumber;
      if ((number == 2) | (number == 4) | (number == 6) | (number == 8)) {
        this.vacantnumber = number;
      } else {
        this.vacantnumber = 1;
      }

      //각 층에 해당하는 도형 리스트 리턴하기

      if (!this.floorImageList.get(this.currentSelectedFloor)) {
        alert("도면 이미지가 없습니다");
        console.log(this.getEachFloorSeatList(this.currentSelectedFloor));
        return;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      console.log("currnet floor is " + this.currentSelectedFloor);

      //n 자리 공석 만들기
      for (let i = 0; i <= this.vacantnumber - 1; i++) {
        this.seatid = this.getSeatUUID();
        let VP = VacantPositon[i];
        let group = [];

        let rectangle = new fabric.Rect({
          width: 50,
          height: 50,
          fill: this.getColor(null),
          opacity: 1,
        });

        let textObject = new fabric.IText("", {
          left: 0,
          top: rectangle.height / 3,
          fontSize: 13,
          fill: "black",
        });

        group[i] = new fabric.Group([rectangle, textObject], {
          floor_id: this.currentSelectedFloor,
          seatId: this.seatid, // currentSelectedFloor-seatId
          employee_name: null,
          employee_department: null,
          employee_number: null,
          employee_id: null,
          left: VP.left,
          top: VP.top,
        });

        group[i].on("mouseover", function (e) {
          let group = e.target;
          let asObject = group.toObject(["seatId"]);
          let x = group.toObject(["left"]);

          console.log("seatId = " + asObject.seatId);
          console.log("left = " + x.left);
        });

        group[i].on("mousedown", (e) => {
          let group = e.target;
          if (e.button === 2) {
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
            this.getEmployeeDialog();
          }
        });

        group[i].on("mousedblclick", (e) => {
          //자리이동 UI 넣을 곳
          this.getChangeSeatDialog();
        });

        this.floorCanvas.add(group[i]);

        eachFloorSeatList.push(group[i]);
        this.floorCanvas.renderAll();
      }

      this.floorCanvas.renderAll();

      console.log("전체층의 자리 맵 size = " + this.eachFloorSeatMap.size);
      console.log(
        this.currentSelectedFloor +
          "의 자리 리스트 length = " +
          eachFloorSeatList.length
      );

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
    },

    setVacantSeat(item) {
      console.log(this.eachEmployeeSeatMap.size + "맵의 사이즈입니다.");
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );

      let eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);

      let activeObject = this.floorCanvas.getActiveObject(); //group 객체
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

      eachEmployeeSeatList.push(activeObject);

      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
      console.log(this.eachEmployeeSeatMap.size + "맵의 사이즈입니다.");

      let groupToObject = activeObject.toObject([
        "seatId",
        "employee_id",
        "floor_id",
      ]);
      console.log(
        groupToObject.employee_id +
          "의 자리 리스트 개수는 " +
          this.getEachEmployeeSeatList(groupToObject.employee_id).length +
          "입니다."
      );
      //여기
    },
    /*!!!!!!!!!!!!!!!axios 관련 코드 app.vue에 다 옮길 예정!!!!!!!!!!!!!!!
    seat VM , employee VM 만 보고 view(component) 다루기위함 */

    //아직 구현중에 있습니다.
    clickSaveBtn() {
      //일단 현재 층에 대한 정보만 저장하는 방식으로 코드를 구현 //추후에 상위 Map을 저장 시킬 예정임.
      if (this.allFloorItems) {
        for (let j = 0; j < this.allFloorItems.length; j++) {
          let eachFloorSeatList = this.getEachFloorSeatList(
            this.allFloorItems[j].floor_name
          );
          if (eachFloorSeatList.length > 0) {
            console.log(
              eachFloorSeatList.length +
                this.allFloorItems[j].floor_name +
                "층의 자리 개수입니다."
            );
            for (let i = 0; i < eachFloorSeatList.length; i++) {
              let groupToObject = eachFloorSeatList[i].toObject([
                "seatId",
                "floor_id",
                "left",
                "top",
                "employee_department",
                "employee_id",
              ]);

              let seatData = {};
              seatData.seat_id = groupToObject.seatId;
              seatData.floor = groupToObject.floor_id;
              seatData.x = groupToObject.left;
              seatData.y = groupToObject.top;
              seatData.is_group = false;
              seatData.group_id = null;
              seatData.building_id = "HANCOM01";
              seatData.employee_id = groupToObject.employee_id;
              seatData.width = 50.5;
              seatData.height = 50.5;
              seatData.degree = 0;
              seatData.shape_id = "1";

              this.saveByAxios(seatData, "seats");
            }
          }
        }
      }

      if (this.allFloorItems) {
        for (let j = 0; j < this.allFloorItems.length; j++) {
          let floorData = {};
          floorData.floor_name = this.allFloorItems[j].floor_name;
          floorData.building_id = this.allFloorItems[j].building_id;
          floorData.floor_index = this.allFloorItems[j].floor_index; // 이후에 삭제된 floor tab들 따로 관리해줘서 같은 index 충돌 안나게 해줘야 함.

          console.log(floorData);
          this.saveByAxios(floorData, "floors");
        }
      }
    },
    saveByAxios(data, tableName) {
      axios
        .post(
          "http://" + host + ":" + portNum + "/" + tableName,
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

      // 현재 층 list 다루기
      for (let i = 0; i < this.seats.length; i++) {
        // !!!!!!!!!!공석 고려 하기!!!!!!!
        if (this.seats[i].floor == this.currentSelectedFloor) {
          eachFloorSeatList = this.getEachFloorSeatList(
            this.currentSelectedFloor
          );
          eachEmployeeSeatList = this.getEachEmployeeSeatList(
            this.seats[i].employee_id
          );

          console.log(this.seats[i]);

          group = this.makeGroupInfo(this.seats[i]);

          this.floorCanvas.add(group);
          eachFloorSeatList.push(group);
          console.log(eachFloorSeatList);
          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);

          eachEmployeeSeatList.push(group);

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
        }
        //다른 층 eachFloorSeatList에 넣기
        else if (this.seat[i].floor != this.currentSelectedFloor) {
          eachFloorSeatList = this.getEachFloorSeatList(this.seat[i].floor);
          eachEmployeeSeatList = this.getEachEmployeeSeatList(
            this.seats[i].employee_id
          );

          console.log(this.seats[i]);

          group = this.makeGroupInfo(this.seats[i]);

          eachFloorSeatList.push(group);

          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);

          eachEmployeeSeatList.push(group);

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
        }
      }
    },
    getEmployeeObjcet(employee_id) {
      // seat table의 employee_id를 받으면 그에 맞는 정보 알아오기 위함
      // group 만들때 필요한 employee 정보 : department, name, number
      let employeeInfoList = new Array();
      let employeeObject = {}; // return 될 Object
      for (let i = 0; i < this.employees.length; i++) {
        let employee = {};
        employee.name = this.employees[i].name;
        employee.department = this.employees[i].department;
        employee.number = this.employees[i].number;
        employee.employee_id = this.employees[i].employee_id;

        employeeInfoList.push(employee);
      }
      console.log(employeeInfoList); // employees

      if (employee_id == null) {
        // 공석
        let employee = {};
        employee.name = null;
        employee.department = null;
        employee.number = null;
        employee.employee_id = null;

        employeeObject = employee;
      } else {
        // 매핑된 자리
        for (let i = 0; i < employeeInfoList.length; i++) {
          if (employee_id == employeeInfoList[i].employee_id) {
            employeeObject = employeeInfoList[i];
          }
        }
      }

      console.log(employeeObject);
      return employeeObject; // return 받아서 department, name, number 뽑아쓰기
    },
    makeGroupInfo(seat) {
      let employee = this.getEmployeeObjcet(seat.employee_id);

      let rectangle = new fabric.Rect({
        width: seat.width,
        height: seat.height,
        fill: this.getColor(employee.department),
      });

      let textObject = null;

      if (seat.employee_id == null) {
        textObject = new fabric.IText("", {
          left: 0,
          top: rectangle.height / 3,
          fontSize: 13,
          fill: "black",
        });
      } else {
        textObject = new fabric.IText(employee.name, {
          left: 0,
          top: rectangle.height / 3,
          fontSize: 13,
          fill: "black",
        });
      }

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
      group.on("mousedown", (e) => {
        let group = e.target;
        if (e.button === 2) {
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
          this.getEmployeeDialog();
        }
      });
      group.on("mousedblclick", (e) => {
        this.getChangeSeatDialog();
      });

      return group;
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
