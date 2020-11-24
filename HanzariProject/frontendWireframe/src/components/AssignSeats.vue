<template>
  <div>
    <v-toolbar color="black" dark>
      <v-toolbar-title>{{ currentSelectedFloorName }} 층</v-toolbar-title>
      <v-spacer></v-spacer>
      <v-toolbar-items class="hidden-sm-and-down">
        <v-btn @click="clickdeleteAllBtn" text
          ><v-icon large>delete</v-icon>모든 좌석 삭제</v-btn
        >
        <v-divider vertical></v-divider>
        <v-btn @click="clickResetToRatioBtn" text
          ><v-icon large>zoom_out</v-icon> 배율 초기화
        </v-btn>
        <v-divider vertical></v-divider>
        <v-btn @click="clickSaveBtn" text
          ><v-icon large>save</v-icon> 저장
        </v-btn>
        <v-divider vertical></v-divider>
        <v-btn @click="clickPrintBtn" text>
          <v-icon large>print</v-icon>프린트
        </v-btn>
        <v-divider vertical></v-divider>
        <v-btn @click="clickExportToCSVBtn" text>
          <v-icon large>cloud_download</v-icon> CSV 내려받기
        </v-btn>
        <v-divider vertical></v-divider>
        <input
          v-show="false"
          ref="Upload"
          type="file"
          accept=".csv, application/vnd.openxmlformats-officedocument.spreadsheetml.sheet, application/vnd.ms-excel"
          @change="changeCSVFile"
        />
        <v-btn @click="$refs.Upload.click()" text>
          <v-icon large>cloud_upload</v-icon> CSV 내용 db 업데이트하기
        </v-btn>
      </v-toolbar-items>
    </v-toolbar>
    <canvas
      ref="canvas"
      class="canvas"
      id="canvas"
      height="770px"
      width="1150px"
      style="text-align: center"
    ></canvas>

    <v-menu
      v-model="contextMenuStatus"
      :position-x="contextMenuXLocation"
      :position-y="contextMenuYLocation"
      absolute
      offset-y
      max-width="180"
    >
      <v-list>
        <v-list-item
          v-for="(item, index) in contextMenuItems"
          :key="index"
          @click="clickContextMenu(item.index)"
        >
          <v-list-item-title>{{ item.title }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
    <v-tooltip
      top
      v-model="toolTipStatus"
      :position-x="toolTipXLocation"
      :position-y="toolTipYLocation"
      :color="toolTipColor"
    >
      <span v-html="toolTipText"> </span>
    </v-tooltip>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
export default {
  name: "AttachCanvas",
  props: [
    "copyEmployeeList",
    "copyFloorList",
    "copyLatestFloorImage",
    "copyOtherFloorsImageList",
    "copyLatestFloorSeatList",
    "copyOtherFloorsSeatMap",
  ],
  data() {
    return {
      floorCanvas: null,

      zoom: 1,
      fontSize: 25,
      clipboard: null,

      currentSelectedFloorName: null,
      currentSelectedFloorId: null,

      latestFloorImageFromDb: this.copyLatestFloorImage,
      otherFloorImageFromDb: this.copyOtherFloorsImageList,
      allImageMap: null, //모든 이미지 저장과 로드(floorId / file or url)

      latestFloorSeatListFromDb: this.copyLatestFloorSeatList,
      otherFloorSeatListFromDb: this.copyOtherFloorsSeatMap,

      allSeatMap: null, //자리 Map<층이름, 자리리스트>
      deleteSeatIdList: [], // deleteId 자리 리스트

      employeeList: this.copyEmployeeList,
      eachEmployeeSeatMap: null, //each Employee's seats map

      allFloorList: this.copyFloorList, // 가시적 층 리스트
      deleteFloorIdList: [],

      contextMenuStatus: false,
      contextMenuXLocation: 100,
      contextMenuYLocation: 100,
      contextMenuItems: [
        { title: "삭제하기", index: 0 },
        { title: "복제하기", index: 1 },
      ],

      toolTipStatus: false,
      toolTipXLocation: 100,
      toolTipYLocation: 100,
      toolTipColor: null,
      toolTipText: null,

      addVacantSwitchStatus: false,

      seatNumber: 0,

      firstMouseDownX: 0,
      firstMouseDownY: 0,
    };
  },
  created() {
    if (this.allFloorList && this.allFloorList.length) {
      this.currentSelectedFloorName = this.allFloorList[
        this.allFloorList.length - 1
      ].floorName;
      this.currentSelectedFloorId = this.allFloorList[
        this.allFloorList.length - 1
      ].floorId;
    }

    if (this.allImageMap == null) {
      this.allImageMap = new Map();
    }
    if (this.allSeatMap == null) {
      this.allSeatMap = new Map();
    }
    if (this.eachEmployeeSeatMap == null) {
      this.eachEmployeeSeatMap = new Map();
    }

    //선택한 층에 대한 값 받아와서 층 전환하기 위한 event
    eventBus.$on("pushSelectedFloorObject", (floorObject) => {
      if (floorObject) {
        this.currentSelectedFloorId = floorObject.floorId;
        this.currentSelectedFloorName = floorObject.floorName;
        this.changeFloor();
      } else {
        this.currentSelectedFloorId = null;
        this.currentSelectedFloorName = null;

        this.floorCanvas.backgroundImage = 0;
        this.floorCanvas.backgroundColor = "aliceblue";
        this.floorCanvas.getObjects().forEach((obj) => {
          this.floorCanvas.remove(obj);
        });
      }
    });

    //층의 이름이 변경될시 화면에 표출되는 string값을 바꾸기 위한 event
    eventBus.$on("pushChangedFloorName", (changedFloorName) => {
      this.currentSelectedFloorName = changedFloorName;
    });

    //공석 만들기 스위치 상태값 변경하기 위한 event
    eventBus.$on("pushAddVacantSwitchStatus", (switchStatus) => {
      this.addVacantSwitchStatus = switchStatus;
    });

    //공석에 사원을 매핑하고자 함수를 호출하기 위한 event
    eventBus.$on("mappingEmployeeToVacant", (employeeObject) => {
      this.mappingEmployeeToVacant(employeeObject);
    });

    //좌석(또는 공석)을 다른층으로 이동하고자 함수를 호출하기 위한 event
    eventBus.$on("moveSeatToAnotherFloor", (floorId) => {
      this.moveSeatToAnotherFloor(floorId);
    });

    //좌석의 사원을 지우고 공석으로 바꾸는 함수를 호출하기 위한 event
    eventBus.$on("changeSeatToVacant", () => {
      if (this.floorCanvas.getActiveObject()) {
        this.changeSeatToVacant();
      } else {
        alert("there is no selected object");
      }
    });

    //모든 층 객체를 가지고 있는 리스트를 받기 위한 event
    eventBus.$on("pushAllFloorList", (allFloorList) => {
      this.allFloorList = allFloorList;
    });

    //이미지 Map 받아오기
    eventBus.$on("pushAllImageMap", (allImageMap) => {
      this.allImageMap = allImageMap;
      this.loadImageFile(
        this.allImageMap.get(this.currentSelectedFloorId).imgPath
      );
    });

    //좌석 하이라이트 하는 함수를 호출하기 위한 event
    eventBus.$on("showSeatHighlight", (seatObject) => {
      this.showSeatHighlight(seatObject);
    });

    //삭제된 층의 아이디를 받기 위한 event
    eventBus.$on("pushDeletedFloorId", (floorId) => {
      this.deleteFloorIdList.push(floorId);

      this.allSeatMap.delete(floorId);
    });
  },
  mounted() {
    this.initializing();
    this.loadLatestFloor(); //최신 층 이미지와 자리 로드
  },
  beforeDestroy() {
    eventBus.$off("moveSeatToAnotherFloor");
    eventBus.$off("pushSelectedFloor");
    eventBus.$off("pushChangedFloorName");
    eventBus.$off("pushAddVacantSwitchStatus");
    eventBus.$off("mappingEmployeeToVacant");
    eventBus.$off("pushAllFloorList");
    eventBus.$off("changeSeatToVacant");
    eventBus.$off("pushAllImageMap");
    eventBus.$off("showSeatHighlight");
    eventBus.$off("pushDeletedFloorId");
  },
  methods: {
    initializing() {
      //canvas, map 생성
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        this.floorCanvas = new fabric.Canvas(ref, {
          fireRightClick: true, //enable firing of right click events
          fireMiddleClick: true, //enable firing of middle click events
          stopContextMenu: true, //prevent context menu from showing
        });

        this.floorCanvas.on("mouse:wheel", (opt) => {
          if (!this.floorCanvas.viewportTransform) {
            return;
          }

          let evt = opt.e;
          let deltaY = evt.deltaY;
          this.zoom = this.floorCanvas.getZoom();
          this.zoom = this.zoom - deltaY / 300;

          if (evt.ctrlKey === true) {
            //zoom in and out
            if (this.zoom > 10) this.zoom = 10;
            else if (this.zoom < 1) this.zoom = 1;

            this.floorCanvas.zoomToPoint(
              new fabric.Point(evt.offsetX, evt.offsetY),
              this.zoom
            );
          } else {
            //reset canvas ratio
            this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
            this.zoom = 1;
          }
          this.checkZoom();

          opt.e.preventDefault();
          opt.e.stopPropagation();
        });

        this.floorCanvas.on("object:moving", function (e) {
          var obj = e.target;
          // if object is too big ignore
          if (
            obj.currentHeight > obj.canvas.height ||
            obj.currentWidth > obj.canvas.width
          ) {
            return;
          }
          obj.setCoords();
          // top-left  corner
          if (obj.getBoundingRect().top < 0 || obj.getBoundingRect().left < 0) {
            obj.top = Math.max(obj.top, obj.top - obj.getBoundingRect().top);
            obj.left = Math.max(
              obj.left,
              obj.left - obj.getBoundingRect().left
            );
          }
          // bot-right corner
          if (
            obj.getBoundingRect().top + obj.getBoundingRect().height >
              obj.canvas.height ||
            obj.getBoundingRect().left + obj.getBoundingRect().width >
              obj.canvas.width
          ) {
            obj.top = Math.min(
              obj.top,
              obj.canvas.height -
                obj.getBoundingRect().height +
                obj.top -
                obj.getBoundingRect().top
            );
            obj.left = Math.min(
              obj.left,
              obj.canvas.width -
                obj.getBoundingRect().width +
                obj.left -
                obj.getBoundingRect().left
            );
          }
        });

        var left1 = 0;
        var top1 = 0;
        var scale1x = 0;
        var scale1y = 0;
        var width1 = 0;
        var height1 = 0;
        this.floorCanvas.on("object:scaling", function (e) {
          var obj = e.target;
          obj.setCoords();
          var brNew = obj.getBoundingRect();

          if (
            brNew.width + brNew.left >= obj.canvas.width ||
            brNew.height + brNew.top >= obj.canvas.height ||
            brNew.left < 0 ||
            brNew.top < 0
          ) {
            obj.left = left1;
            obj.top = top1;
            obj.scaleX = scale1x;
            obj.scaleY = scale1y;
            obj.width = width1;
            obj.height = height1;
          } else {
            left1 = obj.left;
            top1 = obj.top;
            scale1x = obj.scaleX;
            scale1y = obj.scaleY;
            width1 = obj.width;
            height1 = obj.height;
          }
        });

        //원하는 위치에 자동으로 공석 생성하기
        this.floorCanvas.on("mouse:down", (event) => {
          if (event.button === 3) {
            if (this.addVacantSwitchStatus) {
              var pointer = this.floorCanvas.getPointer(event.e);
              this.firstMouseDownX = pointer.x;
              this.firstMouseDownY = pointer.y;

              //console.log(this.firstMouseDownX);
              //console.log(this.firstMouseDownY);
            } else if (this.floorCanvas.getActiveObject()) {
              //contextMenu
              var posX = event.e.clientX;
              var posY = event.e.clientY;
              this.showContextMenuOfOneSeat(posX, posY);
            }
          }
        });

        this.floorCanvas.on("mouse:up", (event) => {
          if (event.button === 3) {
            if (this.addVacantSwitchStatus) {
              var pointer = this.floorCanvas.getPointer(event.e);
              var mouseUpX = pointer.x;
              var mouseUpY = pointer.y;
              //console.log(mouseUpX);
              //console.log(mouseUpY);
              if (
                this.firstMouseDownX === mouseUpX && //Number
                this.firstMouseDownY === mouseUpY //Number
              ) {
                return;
              } else {
                if (!this.allImageMap.get(this.currentSelectedFloorId)) {
                  alert("도면 이미지가 없습니다");
                  return;
                }
                if (this.currentSelectedFloorName == "") {
                  alert("층 이름이 설정되지 않았습니다.");
                  return;
                }
                //console.log(this.firstMouseDownX);
                //console.log(this.firstMouseDownY);
                this.addVacantSeat(
                  this.firstMouseDownX,
                  this.firstMouseDownY,
                  mouseUpX,
                  mouseUpY
                );
              }
            }
          }
        });

        this.floorCanvas.on("object:modified", (e) => {
          this.floorCanvas.getObjects().forEach((obj) => {
            obj.set("httpRequestPostStatus", true);
          });
        });

        this.floorCanvas.on("mouse:over", (event) => {
          let group = event.target;
          if (group != null) {
            var posX = event.e.clientX;
            var posY = event.e.clientY;

            let groupToObject = group.toObject([
              "employeeId",
              "employeeName",
              "employeeDepartment",
              "employeeNumber",
            ]);

            this.showToolTip(
              posX,
              posY,
              groupToObject.employeeId,
              groupToObject.employeeName,
              groupToObject.employeeDepartment,
              groupToObject.employeeNumber
            );
          }
        });

        this.floorCanvas.on("mouse:out", (event) => {
          this.toolTipStatus = false;
        });

        this.manageKeyboard(); //키보드 조작(상하좌우 이동/복붙/삭제)
      }
    },
    clickResetToRatioBtn() {
      this.floorCanvas.setViewportTransform([1, 0, 0, 1, 0, 0]);
    },
    checkZoom() {
      // text, fontSize 관련
      let currentZoom = this.zoom;
      if (5 <= currentZoom && currentZoom <= 7) {
        this.floorCanvas.getObjects().forEach((obj) => {
          if (obj.employeeName) {
            obj.item(1).text = obj.employeeName;
            obj.item(1).fontSize = parseInt(this.fontSize / currentZoom);
          }
        });
      } else if (7 < currentZoom && currentZoom <= 10) {
        this.floorCanvas.getObjects().forEach((obj) => {
          if (obj.employeeName) {
            obj.item(1).text = obj.employeeName + "\n" + obj.employeeNumber;
            obj.item(1).fontSize = parseInt(this.fontSize / currentZoom);
          }
        });
      } else {
        this.floorCanvas.getObjects().forEach((obj) => {
          obj.item(1).text = "";
          obj.item(1).fontSize = parseInt(this.fontSize / currentZoom);
        });
      }
    },
    changeFloor() {
      this.floorCanvas
        .getObjects()
        .slice()
        .forEach((obj) => {
          this.floorCanvas.remove(obj);
        });
      this.floorCanvas.discardActiveObject();

      let eachfloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      this.seatNumber = 0;

      if (this.allImageMap.get(this.currentSelectedFloorId) != null) {
        let typeCheck = this.allImageMap.get(this.currentSelectedFloorId)
          .imgPath;

        if (typeof typeCheck === "string") {
          //url
          this.loadImageUrl(
            this.allImageMap.get(this.currentSelectedFloorId).imgPath
          );
        } else {
          //file
          this.loadImageFile(
            this.allImageMap.get(this.currentSelectedFloorId).imgPath
          );
        }
        //현재 층에 그린 도형들이 있다면
        if (eachfloorSeatList) {
          for (let i = 0; i < eachfloorSeatList.length; i++) {
            this.floorCanvas.add(eachfloorSeatList[i]);
          }
          eventBus.$emit("pushAllSeatMap", this.allSeatMap);
        }
      } else if (this.allImageMap.get(this.currentSelectedFloorId) == null) {
        //현재 층의 이미지가 저장되어있지 않다면 화면에 그려져있던 이미지와 도형 초기화
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            this.floorCanvas.remove(obj);
          });

        this.floorCanvas.backgroundImage = 0;
        this.floorCanvas.backgroundColor = "aliceblue";
        this.floorCanvas.renderAll();

        eventBus.$emit("pushAllSeatMap", this.allSeatMap);
      }
    },
    loadImageFile(file) {
      let reader = new FileReader();
      reader.onload = (e) => {
        this.loadImageUrl(e.target.result);
      };
      reader.readAsDataURL(file);
    },
    loadImageUrl(imgUrl) {
      fabric.Image.fromURL(
        imgUrl,
        (img) => {
          img.set({
            scaleX: this.floorCanvas.width / img.width,
            scaleY: this.floorCanvas.height / img.height,
          });
          this.floorCanvas.setBackgroundImage(
            img,
            this.floorCanvas.renderAll.bind(this.floorCanvas)
          );
        },
        { crossOrigin: "Anonymous" }
      );
    },
    //각 층의 도형 리스트 반환하기
    getEachFloorSeatList: function (floor) {
      if (!floor) {
        // 초반에 층이 생성 안되었을때
        return;
      }
      //층에 해당하는 도형리스트가 만들어지지 않았을때 각 층의 도형 리스트 생성하기
      if (!this.allSeatMap.get(floor)) {
        let newSeatsList = [];
        this.allSeatMap.set(floor, newSeatsList);
        //console.log(this.allSeatMap.size + "allSeatMap 처음의 자리 맵 사이즈입니다");
        return this.allSeatMap.get(floor);
      } else {
        //console.log(this.allSeatMap.size + "allSeatMap 현재 자리 맵 사이즈입니다" );
        return this.allSeatMap.get(floor);
      }
    },
    getEachEmployeeSeatList: function (employeeId) {
      if (!this.eachEmployeeSeatMap.get(employeeId)) {
        let newEmployeeSeatList = [];
        this.eachEmployeeSeatMap.set(employeeId, newEmployeeSeatList);
        return this.eachEmployeeSeatMap.get(employeeId);
      } else {
        return this.eachEmployeeSeatMap.get(employeeId);
      }
    },
    // 해당 층의 도형 리스트의 Delete field 전체 true 만들기
    deleteEachFloorSeatList: function (floor) {
      let eachFloorSeatList = this.getEachFloorSeatList(floor);
      for (let i = 0; i < eachFloorSeatList.length; i++) {
        if (eachFloorSeatList[i].isObjFromDB) {
          let deleteSeat = {};
          deleteSeat.floorId = eachFloorSeatList[i].floorId;
          deleteSeat.seatId = eachFloorSeatList[i].seatId;
          this.deleteSeatIdList.push(deleteSeat);
        }
      }
    },
    //사원의 자리리스트에서 삭제된 자리를 삭제하기
    deleteEachEmployeeSeatList: function (groupToObject) {
      if (groupToObject.employeeId != null) {
        let oneEmployeeSeatList = this.getEachEmployeeSeatList(
          groupToObject.employeeId
        );

        if (oneEmployeeSeatList) {
          for (let i = 0; i < oneEmployeeSeatList.length; i++) {
            if (oneEmployeeSeatList[i].seatId === groupToObject.seatId) {
              //String
              oneEmployeeSeatList.splice(i, 1);
            }
          }
        }
      }
    },
    manageKeyboard() {
      let ctrlDown = false;
      document.addEventListener("keydown", (event) => {
        var key = window.event ? window.event.keyCode : event.keyCode;
        switch (key) {
          case 17:
            ctrlDown = true;
            //console.log("ctrl true");
            break;
          case 37: // left
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("left", this.floorCanvas.getActiveObject().left - 5);
              this.floorCanvas
                .getActiveObject()
                .set("httpRequestPostStatus", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 39: // right
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("left", this.floorCanvas.getActiveObject().left + 5);
              this.floorCanvas
                .getActiveObject()
                .set("httpRequestPostStatus", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 38: // up
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("top", this.floorCanvas.getActiveObject().top - 5);
              this.floorCanvas
                .getActiveObject()
                .set("httpRequestPostStatus", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 40: // down
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("top", this.floorCanvas.getActiveObject().top + 5);
              this.floorCanvas
                .getActiveObject()
                .set("httpRequestPostStatus", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 110:
          case 46: // delete
            this.deleteBtn();
            break;
        }
        if (ctrlDown) {
          switch (key) {
            case 67:
              this.copySelectedSeat();
              break;
            case 86:
              this.pasteSelectedSeat();
              break;
          }
        }
      });
      document.addEventListener("keyup", (event) => {
        var key = window.event ? window.event.keyCode : event.keyCode;
        switch (key) {
          case 17:
            ctrlDown = false;
            //console.log("ctrl false");
            break;
        }
      });
    },
    createSeatUUID() {
      return "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, function (
        c
      ) {
        let r = (Math.random() * 16) | 0,
          v = c == "x" ? r : (r & 3) | 8;
        return v.toString(16);
      });
    },
    getColor(department) {
      const Colors = {
        Orange: "orange",
        Yellow: "yellow",
        Green: "green",
        Blue: "blue",
        Gray: "Gray",
      };
      if (department == null) {
        return Colors.Gray;
      } else {
        if (department === "부서1")
          //String
          return Colors.Orange;
        else if (department === "부서2")
          //String
          return Colors.Yellow;
        else if (department === "부서3")
          //String
          return Colors.Green;
      }
    },

    addVacantSeat(mouseDownX, mouseDownY, mouseUpX, mouseUpY) {
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      let rectangle = new fabric.Rect({
        width: Math.abs(mouseDownX - mouseUpX),
        height: Math.abs(mouseDownY - mouseUpY),
        fill: this.getColor(null),
        opacity: 1,
      });

      let textObject = new fabric.IText("", {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 0,
        fill: "black",
      });

      let group = new fabric.Group([rectangle, textObject], {
        seatId: this.createSeatUUID(),
        floorId: this.currentSelectedFloorId,
        employeeName: null,
        employeeDepartment: null,
        employeeNumber: null,
        employeeId: null,
        left: mouseDownX,
        top: mouseDownY,
        angle: 0,
        isObjFromDB: false,
        httpRequestPostStatus: true,
      });

      if (this.currentSelectedFloorName != null) {
        if (this.getEachFloorSeatList(this.currentSelectedFloorId).length) {
          let seatNumberArray = [];
          this.getEachFloorSeatList(this.currentSelectedFloorId).forEach(
            (seat) => {
              seatNumberArray.push(seat.seatName.split("-")[1]);
            }
          );
          //max
          this.seatNumber = Math.max.apply(null, seatNumberArray);
        }

        this.seatNumber++;
        let seatNameText =
          this.currentSelectedFloorName + "-" + this.seatNumber;

        let seatNameObject = new fabric.IText(seatNameText, {
          left: group.item(0).left,
          top: group.item(0).top - 15,
          fontSize: this.fontSize / this.zoom,
          fill: "black",
        });
        group.seatName = seatNameText;
        group.add(seatNameObject);
      }

      this.floorCanvas.setActiveObject(group);
      this.floorCanvas.add(group);
      this.floorCanvas.renderAll();

      eachFloorSeatList.push(group);

      eventBus.$emit("pushAllSeatMap", this.allSeatMap);
    },
    mappingEmployeeToVacant(employeeObject) {
      if (!this.floorCanvas.getActiveObject()) {
        alert("선택된 좌석이 없습니다.");
        return;
      }

      let mappedOtherEmployeeSeatNameList = [];
      this.floorCanvas.getActiveObjects().forEach((obj) => {
        if (obj.employeeId && obj.employeeId != employeeObject.employeeId) {
          mappedOtherEmployeeSeatNameList.push(obj.seatName);
        }
      });

      mappedOtherEmployeeSeatNameList.forEach((obj) => {
        //console.log(obj);
      });

      if (mappedOtherEmployeeSeatNameList.length > 0) {
        if (
          !confirm(
            "선택된 좌석 중 " +
              mappedOtherEmployeeSeatNameList.join(", ") +
              "자리에 다른 사원이 매핑되어 있습니다.\n" +
              "변경하시겠습니까?"
          )
        ) {
          return;
        }
      }

      let eachEmployeeSeatList = this.getEachEmployeeSeatList(
        employeeObject.employeeId
      );

      this.floorCanvas.getActiveObjects().forEach((obj) => {
        if (obj.employeeId && obj.employeeId != employeeObject.employeeId) {
          // 다른 사원이 매핑된 좌석
          let groupToObject = obj.toObject(["seatId", "employeeId"]);
          this.deleteEachEmployeeSeatList(groupToObject);
        }
        // 다른 사원이 매핑된 좌석 + 공석
        obj.employeeName = employeeObject.name;
        obj.employeeDepartment = employeeObject.department;
        obj.employeeNumber = employeeObject.number;
        obj.employeeId = employeeObject.employeeId;
        obj.item(0).set("fill", this.getColor(obj.employeeDepartment));
        obj.set("httpRequestPostStatus", true);
        eachEmployeeSeatList.push(obj);
      });

      this.checkZoom();
      this.floorCanvas.renderAll();
      eventBus.$emit("pushAllSeatMap", this.allSeatMap);
      eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    clickdeleteAllBtn() {
      if (confirm("Are you sure?")) {
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            let groupToObject = obj.toObject(["seatId", "employeeId"]);
            this.deleteEachEmployeeSeatList(groupToObject);
            this.floorCanvas.remove(obj);
          });
        this.deleteEachFloorSeatList(this.currentSelectedFloorId);
        this.getEachFloorSeatList(this.currentSelectedFloorId).length = 0;

        eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
      } else {
        return;
      }
    },
    deleteBtn() {
      let activeObject = null;
      let shapearray = [];

      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      if (confirm("Are you sure?")) {
        this.floorCanvas.getActiveObjects().forEach((obj) => {
          if (obj.isObjFromDB) {
            let deleteSeat = {};
            deleteSeat.floorId = obj.floorId;
            deleteSeat.seatId = obj.seatId;
            this.deleteSeatIdList.push(deleteSeat);
          }

          let groupToObject = obj.toObject(["seatId", "employeeId"]);
          this.deleteEachEmployeeSeatList(groupToObject);
          //console.log(this.eachEmployeeSeatMap);
          //console.log(eachFloorSeatList);

          let index = eachFloorSeatList.indexOf(obj);
          eachFloorSeatList.splice(index, 1);
          //console.log(eachFloorSeatList);
          this.floorCanvas.remove(obj);

          eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
        });
      } else {
        return;
      }
    },
    changeSeatToVacant() {
      if (confirm("자리를 비우시겠습니까?")) {
        this.floorCanvas.getActiveObjects().forEach((obj) => {
          let groupToObject = obj.toObject(["seatId", "employeeId"]);
          this.deleteEachEmployeeSeatList(groupToObject);
          obj.set("httpRequestPostStatus", true);
          obj.set("employeeName", null);
          obj.set("employeeDepartment", null);
          obj.set("employeeNumber", null);
          obj.set("employeeId", null);
          obj.item(0).set("fill", this.getColor(obj.employeeDepartment));
          this.floorCanvas.remove(obj.item(1)); // delete textObject
          obj.item(1).set("text", "");
        });

        this.floorCanvas.renderAll();
        eventBus.$emit("pushAllSeatMap", this.allSeatMap);
        eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
      } else {
        return;
      }
    },
    //복제하기 (컨텍스트 메뉴 내부)
    cloneSeat() {
      this.copySelectedSeat();
      this.pasteSelectedSeat();
    },
    //clone하기 (ctrl+c)
    copySelectedSeat() {
      if (!this.floorCanvas.getActiveObject()) return;
      //this.clipboard = null;
      this.floorCanvas.getActiveObject().clone((cloned) => {
        this.clipboard = cloned;
      });

      this.clipboard.remove(this.clipboard.item(2));
    },
    //paste하기 (ctrl+v)
    pasteSelectedSeat() {
      if (!this.floorCanvas.getActiveObject()) return;
      let activeObject = this.floorCanvas.getActiveObject();
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let eachEmployeeSeatList = this.getEachEmployeeSeatList(
        this.floorCanvas.getActiveObject().employeeId
      );

      this.clipboard.clone((clonedObj) => {
        this.floorCanvas.discardActiveObject();
        clonedObj.set({
          left: clonedObj.left + 10,
          top: clonedObj.top + 10,
          isObjFromDB: false,
          httpRequestPostStatus: true,
          seatId: this.createSeatUUID(),
          floorId: this.currentSelectedFloorId,
          angle: activeObject.angle,
          employeeDepartment: activeObject.employeeDepartment,
          employeeId: activeObject.employeeId,
          employeeName: activeObject.employeeName,
          employeeNumber: activeObject.employeeNumber,
          evented: true,
        });

        let seatNumberArray = [];
        this.getEachFloorSeatList(this.currentSelectedFloorId).forEach(
          (seat) => {
            seatNumberArray.push(seat.seatName.split("-")[1]);
          }
        );

        this.seatNumber = Math.max.apply(null, seatNumberArray);

        this.seatNumber++;

        clonedObj.seatName =
          this.currentSelectedFloorName + "-" + this.seatNumber;

        let seatNameObject = new fabric.IText(clonedObj.seatName, {
          left: clonedObj.item(0).left,
          top: clonedObj.item(0).top - 15,
          fontSize: this.fontSize / this.zoom,
          fill: "black",
        });
        clonedObj.add(seatNameObject);

        if (clonedObj.type === "activeSelection") {
          clonedObj.canvas = this.floorCanvas;
          clonedObj.forEachObject(function (obj) {
            this.floorCanvas.add(obj);
          });
          // this should solve the unselectability
          clonedObj.setCoords();
        } else {
          this.floorCanvas.add(clonedObj);
        }
        this.clipboard.top += 10;
        this.clipboard.left += 10;

        this.floorCanvas.setActiveObject(clonedObj);
        this.floorCanvas.requestRenderAll();

        eachFloorSeatList.push(clonedObj);
        eachEmployeeSeatList.push(clonedObj);
      });

      eventBus.$emit("pushAllSeatMap", this.allSeatMap);
      eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    showContextMenuOfOneSeat(clientX, clientY) {
      this.contextMenuStatus = false;
      this.contextMenuXLocation = clientX + 10;
      this.contextMenuYLocation = clientY;
      this.$nextTick(() => {
        this.contextMenuStatus = true;
      });
    },
    clickContextMenu(index) {
      switch (index) {
        case 0:
          this.deleteBtn();
          break;
        case 1:
          this.cloneSeat();
          break;
      }
    },
    showToolTip(
      clientX,
      clientY,
      employeeId,
      employeeName,
      employeeDepartment,
      employeeNumber
    ) {
      this.toolTipXLocation = clientX;
      this.toolTipYLocation = clientY;

      if (employeeId == null) {
        this.toolTipText = "공석";
        this.toolTipColor = null;
      } else {
        this.toolTipText =
          "이름 : " +
          employeeName +
          "<br>아이디 : " +
          employeeId +
          "<br>부서 : " +
          employeeDepartment +
          "<br>내선번호 : " +
          employeeNumber;

        this.toolTipColor = "pink lighten-2";
      }

      this.toolTipStatus = true;
    },
    // 층간이동
    moveSeatToAnotherFloor(floorId) {
      if (!this.floorCanvas.getActiveObject()) {
        alert("이동할 좌석이 선택되지 않았습니다.");
        return;
      }
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      for (let i = 0; i < this.allFloorList.length; i++) {
        if (this.allFloorList[i].floorId === floorId) {
          //String
          this.floorCanvas.getActiveObjects().forEach((obj) => {
            obj.set("floorId", this.allFloorList[i].floorId);
            obj.set("floorName", this.allFloorList[i].floorName);
            obj.set("httpRequestPostStatus", true);

            let changeFloorSeatList = this.getEachFloorSeatList(
              this.allFloorList[i].floorId
            );

            changeFloorSeatList.push(obj);

            //이동 후에 원래 list에서 삭제
            for (let j = 0; j < eachFloorSeatList.length; j++) {
              if (eachFloorSeatList[j].seatId === obj.seatId) {
                //String
                eachFloorSeatList[j].set("isObjFromDB", true);
                eachFloorSeatList.splice(j, 1);
              }
            }
            eventBus.$emit("pushFloorOfSeat", this.allFloorList[i].floorId);
            eventBus.$emit("pushEachEmployeeSeatMap", this.eachEmployeeSeatMap);
          });
          this.floorCanvas.renderAll();
        }
      }
    },
    showSeatHighlight(seatObject) {
      //좌석 하이라이트
      let seatFloor = null;
      //seat의 층과 현재층이 같지 않다면
      if (this.currentSelectedFloorId != seatObject.floorId) {
        //탭 전환 코드
        seatFloor = seatObject.floorId;
      }
      //seat의 층과 현재층이 같다면
      else {
        seatFloor = this.currentSelectedFloorId;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(seatFloor);
      for (let i = 0; i < eachFloorSeatList.length; i++) {
        let group = eachFloorSeatList[i];
        let asObject = group.toObject([
          "employeeId",
          "floorId",
          "seatId",
          "employeeDepartment",
        ]);

        let objectSeatId = asObject.seatId;
        if (seatObject.seatId === objectSeatId) { //String
          this.floorCanvas
            .getObjects()
            .slice()
            .forEach((obj) => {
              this.floorCanvas.remove(obj);
            });

          //각 층의 저장된 도형 리스트 화면에 뿌려주기
          //현재 층의 이미지가 저장되어있다면
          if (this.allImageMap.get(seatFloor) != null) {
            let getImageTypeForTypeCheck = this.allImageMap.get(
              this.currentSelectedFloorId
            ).imgPath;
            if (typeof getImageTypeForTypeCheck === "string") {
              //url
              this.loadImageUrl(
                this.allImageMap.get(this.currentSelectedFloorId).imgPath
              );
            } else {
              //file
              this.loadImageFile(
                this.allImageMap.get(this.currentSelectedFloorId).imgPath
              );
            }

            for (let i = 0; i < eachFloorSeatList.length; i++) {
              this.floorCanvas.add(eachFloorSeatList[i]);
            }
          }
          group.item(0).set("opacity", 0);
          group.item(0).set("stroke", "blue");
          group.item(0).set("strokeWidth", 5);

          group.item(0).animate("opacity", 1, {
            duration: 2000,
            onChange: this.floorCanvas.renderAll.bind(this.floorCanvas),
          });
          group.item(0).animate("fill", "red", {
            onChange: this.floorCanvas.renderAll.bind(this.floorCanvas),
            duration: 2000,
            onComplete: getOrginItem,
          });

          let color = this.getColor(asObject.employeeDepartment);
          function getOrginItem() {
            group.item(0).set("opacity", 1);
            group.item(0).set("fill", color);
            group.item(0).set("stroke", null);
            group.item(0).set("strokeWidth", null);
          }
        }
        //자리가 아직 없을때 예외처리 하기
      }
    },
    clickExportToCSVBtn() {
      //csv 내려받기 //seatName, employeeid, floorId
      let floorId = this.currentSelectedFloorId;
      this.$emit("downloadCSVFile", floorId);
      //}
    },
    changeCSVFile(e) {
      let files = e.target.files || e.dataTransfer.files;
      if (!files.length) return;
      this.saveFromCSVFileToDB(files[0]);
    },
    saveFromCSVFileToDB(csvFile) {
      //csv 수정했을시에 db로 정보 save하기
      var newFileForCSVType = new File([csvFile], csvFile.name, {
        type: "text/csv",
      });
      //console.log(newFileForCSVType);

      let floorId = this.currentSelectedFloorId;
      let csvFileData = new FormData();
      csvFileData.append("file", newFileForCSVType);

      this.$emit("saveFromCSVFileToDB", csvFileData, floorId);
    },

    clickSaveBtn() {
      this.floorCanvas.discardActiveObject();

      if (this.allFloorList) {
        //층 저장
        if (this.deleteFloorIdList.length) {
          for (let i = 0; i < this.deleteFloorIdList.length; i++) {
            let deleteFloorKey = this.deleteFloorIdList[i];
            this.$emit("deleteFloorWithKey", "floors", deleteFloorKey);
          }
        }

        for (let i = 0; i < this.allFloorList.length; i++) {
          if (this.allFloorList[i].httpRequestPostStatus) {
            let floorData = {};
            floorData.floor_id = this.allFloorList[i].floorId;
            floorData.floor_name = this.allFloorList[i].floorName;
            floorData.building_id = this.allFloorList[i].buildingId;
            floorData.floor_order = this.allFloorList[i].floorOrder;

            this.$emit("saveFloors", "floors", floorData);
          }
        }

        //이미지 저장
        for (let i = 0; i < this.allFloorList.length; i++) {
          let imgData = new FormData();
          let floorId = this.allFloorList[i].floorId;

          //console.log(this.allImageMap.get(floorId));

          if (this.allImageMap.get(floorId) != null) {
            let file = this.allImageMap.get(floorId).imgPath;
            if (typeof file === "string") {
              //url
            } else {
              //file
              imgData.append("imageFile", file);
              this.$emit("saveImages", "images", imgData, floorId);
            }
          }
        }

        //자리 저장
        if (this.deleteSeatIdList.length > 0) {
          for (let i = 0; i < this.deleteSeatIdList.length; i++) {
            let deleteSeatKey = this.deleteSeatIdList[i].seatId;
            let deleteSeatFloor = this.deleteSeatIdList[i].floorId;
            this.$emit(
              "deleteSeatWithKey",
              "seats",
              deleteSeatKey,
              deleteSeatFloor
            );
          }
        }
        for (let i = 0; i < this.allFloorList.length; i++) {
          let eachFloorSeatList = this.getEachFloorSeatList(
            this.allFloorList[i].floorId
          );

          if (eachFloorSeatList.length > 0) {
            for (let j = 0; j < eachFloorSeatList.length; j++) {
              let groupToObject = eachFloorSeatList[j].toObject([
                "seatId",
                "seatName",
                "floorId",
                "left",
                "top",
                "employeeDepartment",
                "employeeId",
                "width",
                "height",
                "scaleX",
                "scaleY",
                "isObjFromDB",
                "httpRequestPostStatus",
              ]);
              
              if (groupToObject.httpRequestPostStatus) {
                let seatData = {};
                seatData.seat_id = groupToObject.seatId;
                seatData.seat_name = groupToObject.seatName;
                seatData.floor = groupToObject.floorId;
                seatData.x = groupToObject.left;
                seatData.y = groupToObject.top;
                seatData.is_group = false;
                seatData.group_id = null;
                seatData.building_id = "HANCOM01";
                seatData.employee_id = groupToObject.employeeId;
                seatData.width = groupToObject.width * groupToObject.scaleX;
                seatData.height = groupToObject.height * groupToObject.scaleY;
                seatData.degree = groupToObject.angle;
                seatData.shape_id = "1";

                this.$emit("saveSeats", "seats", seatData, seatData.floor);
              }
            }
          }
        }
      }
    },
    getEmployeeObject(employeeId) {
      // seat table의 employeeId를 받으면 그에 맞는 정보 알아오기 위함
      let employeeInfoList = [];
      let employeeObject = {}; // return 될 Object
      for (let i = 0; i < this.employeeList.length; i++) {
        let employee = {};
        employee.name = this.employeeList[i].name;
        employee.department = this.employeeList[i].department;
        employee.number = this.employeeList[i].number;
        employee.employeeId = this.employeeList[i].employeeId;

        employeeInfoList.push(employee);
      }

      if (employeeId == null) {
        // 공석
        let employee = {};
        employee.name = null;
        employee.department = null;
        employee.number = null;

        employeeObject = employee;
      } else {
        // 매핑된 자리
        for (let i = 0; i < employeeInfoList.length; i++) {
          if (employeeId === employeeInfoList[i].employeeId) {
            //String
            employeeObject = employeeInfoList[i];
          }
        }
      }
      return employeeObject; // return 받아서 department, name, number 뽑아쓰기
    },
    makeGroupInfo(seat) {
      let employee = this.getEmployeeObject(seat.employeeId);

      let rectangle = new fabric.Rect({
        width: seat.width,
        height: seat.height,
        fill: this.getColor(employee.department),
        opacity: 1,
      });

      let textObject = new fabric.IText("", {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 0,
        fill: "black",
      });

      let group = new fabric.Group([rectangle, textObject], {
        seatId: seat.seatId,
        employeeName: employee.name,
        employeeDepartment: employee.department,
        employeeNumber: employee.number,
        employeeId: seat.employeeId,
        floorId: seat.floorId,
        left: seat.x,
        top: seat.y,
        angle: seat.degree,
        isObjFromDB: seat.isObjFromDB,
        httpRequestPostStatus: seat.httpRequestPostStatus,
      });

      let seatNameObject = new fabric.IText(seat.seatName, {
        left: rectangle.left,
        top: rectangle.top - 15,
        fontSize: this.fontSize / this.zoom,
        fill: "black",
      });

      group.seatName = seat.seatName;
      group.add(seatNameObject);

      this.floorCanvas.on("object:scaling", (e) => {
        let scaledObject = e.target;
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
      });

      return group;
    },
    loadLatestFloor() {
      //현재 층 이미지 로드
      if (this.latestFloorImageFromDb) {
        for (let i = 0; i < this.latestFloorImageFromDb.length; i++) {
          let newImageObject = {};
          newImageObject.imgPath = this.latestFloorImageFromDb[i].imgPath;
          newImageObject.floorId = this.latestFloorImageFromDb[i].floorId;
          newImageObject.imgFileName = this.latestFloorImageFromDb[
            i
          ].imgFileName;

          //console.log("받아온 이미지 파일명입니다.");
          //console.log(newImageObject.imgFileName);

          this.allImageMap.set(newImageObject.floorId, newImageObject);

          this.currentSelectedFloorId = newImageObject.floorId;

          this.loadImageUrl(newImageObject.imgPath);
          // 현재층 자리 로드

          if (this.latestFloorSeatListFromDb.length) {
            for (let i = 0; i < this.latestFloorSeatListFromDb.length; i++) {
              this.currentSelectedFloorId = this.latestFloorSeatListFromDb[
                i
              ].floorId;

              let eachFloorSeatList = this.getEachFloorSeatList(
                this.latestFloorSeatListFromDb[i].floorId
              );

              let group = this.makeGroupInfo(this.latestFloorSeatListFromDb[i]);

              this.floorCanvas.add(group);
              eachFloorSeatList.push(group);

              eventBus.$emit("pushAllSeatMap", this.allSeatMap);

              if (this.latestFloorSeatListFromDb[i].employeeId != null) {
                let eachEmployeeSeatList = this.getEachEmployeeSeatList(
                  this.latestFloorSeatListFromDb[i].employeeId
                );
                eachEmployeeSeatList.push(group);
                eventBus.$emit(
                  "pushEachEmployeeSeatMap",
                  this.eachEmployeeSeatMap
                );
              }
            }
          } else {
            this.currentSelectedFloorId = newImageObject.floorId;
          }
        }
        this.loadOtherFloors();
      }
    },
    loadOtherFloors() {
      if (this.otherFloorSeatListFromDb) {
        //다른 층 이미지 로드
        for (let i = 0; i < this.otherFloorImageFromDb.length; i++) {
          let newImageObject = {};
          newImageObject.imgPath = this.otherFloorImageFromDb[i].imgPath;
          newImageObject.floorId = this.otherFloorImageFromDb[i].floorId;
          newImageObject.imgFileName = this.otherFloorImageFromDb[
            i
          ].imgFileName;

         // console.log("받아온 이미지 파일명입니다.");
         // console.log(newImageObject.imgFileName);
          this.allImageMap.set(newImageObject.floorId, newImageObject);
        }
        //다른 층 자리 로드
        if (this.otherFloorSeatListFromDb) {
          let keys = [];
          keys = Array.from(this.otherFloorSeatListFromDb.keys());

          for (let i = 0; i < keys.length; i++) {
            let seats = [];
            seats = this.otherFloorSeatListFromDb.get(keys[i]);

            for (let j = 0; j < seats.length; j++) {
              let eachFloorSeatList = this.getEachFloorSeatList(
                seats[j].floorId
              );

              let group = this.makeGroupInfo(seats[j]);
              eachFloorSeatList.push(group);

              if (seats[j].employeeId != null) {
                let eachEmployeeSeatList = this.getEachEmployeeSeatList(
                  seats[j].employeeId
                );
                eachEmployeeSeatList.push(group);
                eventBus.$emit(
                  "pushEachEmployeeSeatMap",
                  this.eachEmployeeSeatMap
                );
              }

              eventBus.$emit("pushAllSeatMap", this.allSeatMap);
            }
          }
        }
      }
    },
    clickPrintBtn() {
      var printWin = window.open("", "", "width=1500,height=800");
      let dateUrl = document.getElementById("canvas").toDataURL();

      var windowContent = "<!DOCTYPE html>";
      windowContent += "<html>";
      windowContent += "<head><title>Hnazari</title>";
      //windowContent += "<style> @media print{.myDivToPrint {background-color: white; height: 100%;width: 100%;position: fixed;top: 0;left: 0;margin: 0;padding: 15px;font-size: 14px;line-height: 18px;}</style>";
      windowContent += "</head>";
      windowContent += "<body>";
      windowContent +=
        '<img src="' + dateUrl + '" onload=window.print();window.close();>';
      windowContent += "</body>";
      windowContent += "</html>";

      printWin.document.write(windowContent);
    },
  },
};
</script>

<style scoped>
.canvas {
  margin-left: 45px;
  border: 1px solid #000;
  background: aliceblue;
  width: 75%;
  height: 100%;
}
ul {
  padding: 0px;
  margin: 0px;
  display: block;
}
</style>
