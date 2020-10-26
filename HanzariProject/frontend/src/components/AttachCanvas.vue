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
    <v-btn @click="clickResetToRatio" color="pink">Reset Ratio</v-btn>
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
import { eventBus } from "../main.js";
import EmployeeDialog from "@/components/EmployeeDialog.vue";
import ChangeSeatDialog from "@/components/ChangeSeatDialog.vue";
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";

export default {
  props: ["seat", "copyEmployee", "currentFloorSeatsList"],
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
      allSeatMap: null, //all seat map
      currentFloorSeatListFromDb: this.currentFloorSeatsList, //current floor's seatList
      eachEmployeeSeatMap: null, //each Employee's seats map
      deleteSeatListKey: null, //삭제되는 층의 floor_name (층 삭제될때 FloorTabs.vue에서 넘어옴)
      employeeDialogStatus: false,
      changeSeatDialogStatus: false,
      inputChangeSeatFloor: null,
      inputChangeSeatX: null,
      inputChangeSeatY: null,
      allEmployeeList: [],
      seats: this.seat, //DB로부터 넘어온 현재 층의 자리들을 제외한 자리 Map <층이름, 자리리스트>
      employees: this.copyEmployee,
      items: [{ number: 2 }, { number: 4 }, { number: 6 }, { number: 8 }],
      allFloorList: [],
      // 자리들 저장할때 managerFloorList를 사용하지 않은 이유는 층이 가시적으로 없어지게 되면
      // managerFloorList에는 남아있고 D필드만 true가 되는데, 가시적으로 없어진 층의 자리들을 저장하는 것은 옳지않다고 판단
      managerFloorList: [],
    };
  },
  created() {
    eventBus.$on("confirmChangeSeatDialog", (inputInfo) => {
      this.confirmChangeSeatDialog(inputInfo);
    });
    eventBus.$on("showSeat", (seat) => {
      this.showSeat(seat);
    });
    eventBus.$on("changeFloor", (floor) => {
      this.currentSelectedFloor = floor;
      this.changeFloor(this.currentSelectedFloor);
      console.log(this.currentSelectedFloor + "여기가 현재층");

      this.clickLoadBtn();
    });
    eventBus.$on("allEmployeeList", (allEmployeeList) => {
      this.allEmployeeList = allEmployeeList;
    });
    eventBus.$on("MappingSeat", (item) => {
      this.setVacantSeat(item);
    });
    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      console.log(this.allFloorList);
    });
    eventBus.$on("managerFloorList", (managerFloors) => {
      this.managerFloorList = managerFloors;
      console.log(this.managerFloorList);
    });
    eventBus.$on("deleteSeatListKey", (floor_name) => {
      this.deleteSeatListKey = floor_name;
      console.log(this.deleteSeatListKey + "deleteSeatListKey");

      let eachFloorSeatList = this.deleteEachFloorSeatList(
        this.deleteSeatListKey
      );
      //나중에 managerSeatList에서도 삭제해야함!!!!!!!!!!!!!
    });

    if (this.floorImageList == null) {
      this.floorImageList = new Map();
    }
    if (this.allSeatMap == null) {
      this.allSeatMap = new Map();
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
          } else {
            //reset canvas ratio
            this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
          }
          opt.e.preventDefault();
          opt.e.stopPropagation();
        });
      }
    },
    clickResetToRatio() {
      this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
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
    onFileChange(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.createImage(files[0]);
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
      console.log(this.floorImageList.get(this.currentSelectedFloor));
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
            myGroup.item(0).set("fill", color);
          }
        }
        //자리가 아직 없을때 예외처리 하기
      }
    },
    //각 층의 도형 리스트 반환하기
    getEachFloorSeatList: function (floor) {
      //층에 해당하는 도형리스트가 만들어지지 않았을때 각 층의 도형 리스트 생성하기
      if (!this.allSeatMap.get(floor)) {
        let newSeatsList = new Array();
        this.allSeatMap.set(floor, newSeatsList);
        //console.log(this.allSeatMap.size + "allSeatMap 처음의 자리 맵 사이즈입니다");
        return this.allSeatMap.get(floor);
      } else {
        //console.log(this.allSeatMap.size + "allSeatMap 현재 자리 맵 사이즈입니다" );
        return this.allSeatMap.get(floor);
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
        activeObject = this.floorCanvas.getActiveObject(); // 선택 객체 가져오기

        activeObject.set("modify", true);

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
        if (oneEmployeeSeatList[i].seatId == groupToObject.seatId) {
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
            obj.set("delete", true);
            let groupToObject = obj.toObject(["seatId", "employee_id"]);
            this.deleteEachEmployeeSeatList(groupToObject);
            this.floorCanvas.remove(obj);
          });

        let eachFloorSeatList = this.deleteEachFloorSeatList(
          this.currentSelectedFloor
        );

        eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
        eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
      }
    },

    deleteBtn() {
      //좌석 지우면 list에 있는거 없애기
      let activeObject = null;
      let eachFloorSeatList = this.deleteEachFloorSeatList(
        this.currentSelectedFloor
      );

      let shapearray = new Array();

      if (confirm("Are you sure?")) {
        if (this.floorCanvas.getActiveObjects().length == 1) {
          activeObject = this.floorCanvas.getActiveObject(); //console.log("단일객체 선택");
          activeObject.set("delete", true);

          let groupToObject = activeObject.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
        } else {
          activeObject = this.floorCanvas.getActiveObjects(); //console.log("복수객체 선택");
          activeObject.set("delete", true);

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
              let groupToObject = activeObject.toObject(["seatId"]);
              let index = shapearray.indexOf(activeObject);
              shapearray.splice(index, 1);
            }
          });
          this.floorCanvas.remove(activeObject);
          //modify map(allSeatMap)
          eachFloorSeatList.length = 0;
          this.allSeatMap.set(this.currentSelectedFloor, shapearray);

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
          width: 100,
          height: 100,
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
          angle: 0,
          create: true, //생성
          modify: false, //변경
          delete: false, //삭제
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

        this.floorCanvas.on("object:scaling", (e) => {
          let scaledObject = e.target;
          //console.log("Width =  " + scaledObject.getScaledWidth());
          //console.log("Height = " + scaledObject.getScaledHeight());

          let width = scaledObject.getScaledWidth() / scaledObject.scaleX;
          let height = scaledObject.getScaledHeight() / scaledObject.scaleY;

          scaledObject.width = width;
          scaledObject.height = height;
          let groupx = scaledObject.toObject([
            "width",
            "height",
            "scaleX",
            "scaleY",
          ]);
          //console.log(groupx.width * groupx.scaleX + "저장할 width");
          //console.log(groupx.height * groupx.scaleY + "저장할 height");
        }),
          this.floorCanvas.on("object:modified", function (e) {
            //크기, 이동, 회전
            let modifyObject = e.target;
            modifyObject.set("modify", true);
          });

        this.floorCanvas.add(group[i]);
        eachFloorSeatList.push(group[i]);
        this.floorCanvas.renderAll();
      } // end of for

      this.floorCanvas.renderAll();

      console.log("전체층의 자리 맵 size = " + this.allSeatMap.size);
      console.log(
        this.currentSelectedFloor +
          "의 자리 리스트 length = " +
          eachFloorSeatList.length
      );

      //console.log("allSeatMap size = " + this.allSeatMap.size);
      //console.log("eachFloorSeatList length = " + eachFloorSeatList.length);

      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
    },

    setVacantSeat(item) {
      console.log(this.eachEmployeeSeatMap.size + "맵의 사이즈입니다.");
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloor
      );
      let eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);
      let activeObject = this.floorCanvas.getActiveObject(); //group 객체

      //해당 자리가 사원이 매핑되어있는 상태에서 다른 사원으로 변경하고자 하는 경우
      if (
        activeObject.employee_id != null &&
        activeObject.employee_id != item.employee_id
      ) {
        if (
          confirm(
            activeObject.employee_name +
              "사원의 자리를 " +
              item.name +
              "자리로 변경하시겠습니까?"
          )
        ) {
          let groupToObject = activeObject.toObject([
            "seatId",
            "employee_id",
            "floor_id",
          ]);
          activeObject.set("modify", true);
          this.deleteEachEmployeeSeatList(groupToObject);
        }
      }
      //해당 자리가 사원이 매핑되어있는 상태에서 같은 사원으로 매핑을 한번더 하려고 하는 경우
      else if (
        activeObject.employee_id != null &&
        activeObject.employee_id == item.employee_id
      ) {
        alert("이 자리는 이미 " + item.name + "의 자리입니다.");
        return;
      }

      //해당 자리가 사원이 매핑되어있지않으면 바로 이 시점으로..
      activeObject.employee_name = item.name;
      activeObject.employee_department = item.department;
      activeObject.employee_number = item.number;
      activeObject.employee_id = item.employee_id;
      activeObject
        .item(0)
        .set("fill", this.getColor(activeObject.employee_department));
      activeObject._objects[1].text = item.name;
      activeObject.set("modify", true);
      this.floorCanvas.renderAll();

      eachEmployeeSeatList.push(activeObject);
      eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },

    //아직 구현중에 있습니다.
    clickSaveBtn() {
      if (this.managerFloorList) {
        for (let i = 0; i < this.managerFloorList.length; i++) {
          if (!this.managerFloorList[i].create) {
            // 원본
            if (this.managerFloorList[i].delete) {
              // 001 011 delete
              let deleteFloorKey = this.managerFloorList[i].floor_id;
              this.$emit("deleteFloorByAxiosWithKey", "floors", deleteFloorKey);
            } else if (this.managerFloorList[i].modify) {
              //010 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveByAxios", "floors", floorData);
            }
          } else {
            // front에서 생성
            if (this.managerFloorList[i].delete) {
              //101 111 nothing
              return;
            } else {
              //100 110 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveByAxios", "floors", floorData);
            }
          }
        }
      }

      if (this.allFloorList) {
        for (let j = 0; j < this.allFloorList.length; j++) {
          let eachFloorSeatList = this.getEachFloorSeatList(
            this.allFloorList[j].floor_name
          );

          //사본 eachFloorSeatList
          if (eachFloorSeatList.length > 0) {
            console.log(
              eachFloorSeatList.length +
                this.allFloorList[j].floor_name +
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
                "width",
                "height",
                "scaleX",
                "scaleY",
                "create",
                "modify",
                "delete",
              ]);

              console.log(groupToObject);
              //axios api 호출
              if (groupToObject.create == false) {
                if (groupToObject.delete == true) {
                  //axios.delete
                } else if (groupToObject.modify == true) {
                  //axios.post
                }
              } else {
                //groupToObject.create == true
                if (groupToObject.true == false) {
                  break;
                } else {
                  //axios.post
                }
              }

              let seatData = {};
              seatData.seat_id = groupToObject.seatId;
              seatData.floor = groupToObject.floor_id;
              seatData.x = groupToObject.left;
              seatData.y = groupToObject.top;
              seatData.is_group = false;
              seatData.group_id = null;
              seatData.building_id = "HANCOM01";
              seatData.employee_id = groupToObject.employee_id;
              seatData.width = groupToObject.width * groupToObject.scaleX;
              seatData.height = groupToObject.height * groupToObject.scaleY;
              seatData.degree = groupToObject.angle;
              seatData.shape_id = "1";

<<<<<<< HEAD
              console.log(seatData)
              this.$emit("saveByAxios", "seats", seatData);
=======
              console.log(seatData);
              this.$emit("saveByAxios", seatData, "seats");
>>>>>>> 6e3596c6128596f4a17c4128f2d7a391e7bc41d2
            }
          }
        }
      }
    },
    getEmployeeObject(employee_id) {
      // seat table의 employee_id를 받으면 그에 맞는 정보 알아오기 위함
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
      let employee = this.getEmployeeObject(seat.employee_id);

      let rectangle = new fabric.Rect({
        width: seat.width,
        height: seat.height,
        fill: this.getColor(employee.department),
        opacity: 1,
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
        floor_id: seat.floor,
        left: seat.x,
        top: seat.y,
        angle: seat.degree,
        create: seat.create,
        modify: seat.modify,
        delete: seat.delete,
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
    loadCurrentFloorSeats() {
      /*이후에 내부에 있는 중복 로직은 함수로 뺄 예정 (rectangle, textObject, grouping 과정 및 group의 interaction ) */
      // 현재 층 list 다루기
      for (let i = 0; i < this.currentFloorSeatListFromDb.length; i++) {
        // !!!!!!!!!!공석 고려 하기!!!!!!!
        if (
          this.currentFloorSeatListFromDb[i].floor == this.currentSelectedFloor
        ) {
          console.log(
            "현재층의 자리 개수는 ------> " +
              this.currentFloorSeatListFromDb.length
          ); //4

          let eachFloorSeatList = this.getEachFloorSeatList(
            this.currentFloorSeatListFromDb[i].floor
          );
          let eachEmployeeSeatList = this.getEachEmployeeSeatList(
            this.currentFloorSeatListFromDb[i].employee_id
          );

          console.log(this.currentFloorSeatListFromDb); ////

          let group = this.makeGroupInfo(this.currentFloorSeatListFromDb[i]);

          this.floorCanvas.add(group);
          eachFloorSeatList.push(group);
          eachEmployeeSeatList.push(group);

          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
          eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
          console.log(
            this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
          );
          console.log(
            this.currentFloorSeatListFromDb[i].employee_id +
              "의 자리 리스트 개수는 " +
              this.getEachEmployeeSeatList(
                this.currentFloorSeatListFromDb[i].employee_id
              ).length +
              "입니다."
          );
        }
      }
    },
    clickLoadBtn() {
      for (let i = 0; i < this.currentFloorSeatListFromDb.length; i++) {
        // !!!!!!!!!!공석 고려 하기!!!!!!!
        if (
          this.currentFloorSeatListFromDb[i].floor == this.currentSelectedFloor
        ) {
          console.log(
            "현재층의 자리 개수는 ------> " +
              this.currentFloorSeatListFromDb.length
          ); //4

          let eachFloorSeatList = this.getEachFloorSeatList(
            this.currentFloorSeatListFromDb[i].floor
          );
          let eachEmployeeSeatList = this.getEachEmployeeSeatList(
            this.currentFloorSeatListFromDb[i].employee_id
          );

          console.log(this.currentFloorSeatListFromDb); ////

          let group = this.makeGroupInfo(this.currentFloorSeatListFromDb[i]);

          this.floorCanvas.add(group);
          eachFloorSeatList.push(group);
          eachEmployeeSeatList.push(group);

          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
          eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
          console.log(
            this.eachEmployeeSeatMap.size + "악시오스로 가지온 직원 수입니다."
          );
          console.log(
            this.currentFloorSeatListFromDb[i].employee_id +
              "의 자리 리스트 개수는 " +
              this.getEachEmployeeSeatList(
                this.currentFloorSeatListFromDb[i].employee_id
              ).length +
              "입니다."
          );
        }
      }
      //다른 층 list 저장하기
      for (let i = 0; i < this.seats.size; i++) {
        for (let j = 0; j < this.seats[i].length; j++) {
          let eachFloorSeatList = this.getEachFloorSeatList(this.seat[i].floor);
          let eachEmployeeSeatList = this.getEachEmployeeSeatList(
            this.seats[i].employee_id
          );
          let group = this.makeGroupInfo(this.seats[j]);
          eachFloorSeatList.push(group);
          eachEmployeeSeatList.push(group);
          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
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
