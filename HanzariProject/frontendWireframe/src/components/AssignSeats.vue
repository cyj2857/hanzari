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
      allImageMap: null, //모든 이미지 저장과 로드(floorid / file or url)

      latestFloorSeatListFromDb: this.copyLatestFloorSeatList,
      otherFloorSeatListFromDb: this.copyOtherFloorsSeatMap,
      //자리 Map <층이름, 자리리스트>
      allSeatMap: null, //가시적 자리 map
      managerAllSeatMap: null, //DB 관리 자리 map

      employees: this.copyEmployeeList,
      eachEmployeeSeatMap: null, //each Employee's seats map

      allFloorList: this.copyFloorList, // 가시적 층 리스트
      managerFloorList: this.copyFloorList, // DB 관리 층 리스트

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

      ableAddVacant: false,

      seatWidth: null,
      seatHeight: null,

      seatNumber: 0,

      isDown: null,
      firstMouseDownX: 0,
      firstMouseDownY: 0,
    };
  },
  created() {
    if (this.allFloorList && this.allFloorList.length) {
      this.currentSelectedFloorName = this.allFloorList[
        this.allFloorList.length - 1
      ].floor_name;
      this.currentSelectedFloorId = this.allFloorList[
        this.allFloorList.length - 1
      ].floor_id;
    }
    //층간 이동
    eventBus.$on("clickChangeFloorSeat", (floor_id) => {
      this.changeFloorSeat(floor_id);
    });

    eventBus.$on("changeFloor", (floor) => {
      if (floor) {
        this.currentSelectedFloorId = floor.floor_id;
        this.currentSelectedFloorName = floor.floor_name;
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

    eventBus.$on("changeFloorName", (changeFloorName) => {
      this.currentSelectedFloorName = changeFloorName;
    });

    eventBus.$on("changeAddVacantSwitch", (switchValue) => {
      this.ableAddVacant = switchValue;
    });
    eventBus.$on("setSeatSizeDialog", (seatSize) => {
      this.seatWidth = seatSize.width;
      this.seatHeight = seatSize.height;
    });
    eventBus.$on("mappingSeat", (item) => {
      this.setMappingSeat(item);
    });
    eventBus.$on("allFloorList", (allFloors) => {
      this.allFloorList = allFloors;
      console.log(this.allFloorList);
    });
    eventBus.$on("managerFloorList", (managerFloors) => {
      this.managerFloorList = managerFloors;
      console.log(this.managerFloorList);
    });
    eventBus.$on("changeToVacant", () => {
      if (this.floorCanvas.getActiveObject()) {
        this.changeToVacant();
      } else {
        alert("there is no selected object");
      }
    });
    eventBus.$on("allImageMap", (allImageMap) => {
      this.allImageMap = allImageMap;
      this.loadImageFile(this.allImageMap.get(this.currentSelectedFloorId));
    });
    eventBus.$on("showSeat", (seat) => {
      this.showSeat(seat);
    });
    eventBus.$on("deleteSeatListKey", (floor_id) => {
      this.allSeatMap.delete(floor_id);
      //managerAllSeatMap 에서 삭제되어도 되는 이유 :
      //managerFloorList만큼 저장을 하기때문에 그에 해당되지 않는 key는 저장이 되지 않을 것.
      //그리고 DB에서도 삭제되는 층이 있으면 자동으로 그 층에 해당하는 자리들도 삭제함
      this.managerAllSeatMap.delete(floor_id);
    });
    if (this.allImageMap == null) {
      this.allImageMap = new Map();
    }
    if (this.allSeatMap == null) {
      this.allSeatMap = new Map();
    }
    if (this.managerAllSeatMap == null) {
      this.managerAllSeatMap = new Map();
    }
    if (this.eachEmployeeSeatMap == null) {
      this.eachEmployeeSeatMap = new Map();
    }
  },
  mounted() {
    this.initializing();
    this.loadLatestFloor(); //현재 층 이미지와 자리 로드
  },
  beforeDestroy() {
    eventBus.$off("clickChangeFloorSeat");
    eventBus.$off("changeFloor");
    eventBus.$off("changeFloorName");
    eventBus.$off("changeAddVacantSwitch");
    eventBus.$off("setSeatSizeDialog");
    eventBus.$off("mappingSeat");
    eventBus.$off("allFloorList");
    eventBus.$off("managerFloorList");
    eventBus.$off("changeToVacant");
    eventBus.$off("allImageMap");
    eventBus.$off("showSeat");
    eventBus.$off("deleteSeatListKey");
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
            if (this.ableAddVacant) {
              // if (!this.seatWidth) {
              //   alert("공석 크기를 선택해야 합니다.");
              //   return;
              // }
              var pointer = this.floorCanvas.getPointer(event.e);
              this.firstMouseDownX = pointer.x;
              this.firstMouseDownY = pointer.y;

              console.log(this.firstMouseDownX);
              console.log(this.firstMouseDownY);
            } else if (this.floorCanvas.getActiveObject()) {
              //contextMenu
              var posX = event.e.clientX;
              var posY = event.e.clientY;
              this.showContextMenu(posX, posY);
            }
          }
        });

        this.floorCanvas.on("mouse:up", (event) => {
          if (event.button === 3) {
            if (this.ableAddVacant) {
              var pointer = this.floorCanvas.getPointer(event.e);
              var mouseUpX = pointer.x;
              var mouseUpY = pointer.y;

              console.log(mouseUpX);
              console.log(mouseUpY);
              console.log("mouseup");

              //console.log(typeof this.firstMouseDownX) //number
              //console.log(typeof mouseUpX) //number
              //console.log(typeof this.firstMouseDownY) //number
              //console.log(typeof mouseUpY) //number

              if (
                this.firstMouseDownX === mouseUpX &&
                this.firstMouseDownY === mouseUpY
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

                console.log(this.firstMouseDownX);
                console.log(this.firstMouseDownY);

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
            obj.set("modify", true);
          });
        });

        this.floorCanvas.on("mouse:over", (event) => {
          let group = event.target;
          if (group != null) {
            var posX = event.e.clientX;
            var posY = event.e.clientY;

            let groupToObject = group.toObject([
              "employee_id",
              "employee_name",
              "employee_department",
              "employee_number",
            ]);

            this.showToolTip(
              posX,
              posY,
              groupToObject.employee_id,
              groupToObject.employee_name,
              groupToObject.employee_department,
              groupToObject.employee_number
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
          if (obj.employee_name) {
            obj.item(1).text = obj.employee_name;
            obj.item(1).fontSize = parseInt(this.fontSize / currentZoom);
          }
        });
      } else if (7 < currentZoom && currentZoom <= 10) {
        this.floorCanvas.getObjects().forEach((obj) => {
          if (obj.employee_name) {
            obj.item(1).text = obj.employee_name + "\n" + obj.employee_number;
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
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
        this.currentSelectedFloorId
      );
      this.seatNumber = 0;

      if (this.allImageMap.get(this.currentSelectedFloorId) != null) {
        let typeCheck = this.allImageMap.get(this.currentSelectedFloorId);
        if (typeof typeCheck === "string") {
          //url
          this.loadImageUrl(this.allImageMap.get(this.currentSelectedFloorId));
        } else {
          //file
          this.loadImageFile(this.allImageMap.get(this.currentSelectedFloorId));
        }
        //현재 층에 그린 도형들이 있다면
        if (eachfloorSeatList) {
          for (let i = 0; i < eachfloorSeatList.length; i++) {
            this.floorCanvas.add(eachfloorSeatList[i]);
          }
          eventBus.$emit("allSeatMap", this.allSeatMap);
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

        eventBus.$emit("allSeatMap", this.allSeatMap);
      }
    },
    loadImageFile(file) {
      let reader = new FileReader();
      reader.onload = (e) => {
        this.loadImageUrl(e.target.result);
      };
      reader.readAsDataURL(file);
    },
    loadImageUrl(imgurl) {
      fabric.Image.fromURL(
        imgurl,
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
    getManagerEachFloorSeatList: function (floor) {
      if (!floor) {
        // 초반에 층이 생성 안되었을때
        return;
      }
      if (!this.managerAllSeatMap.get(floor)) {
        let newSeatsList = [];
        this.managerAllSeatMap.set(floor, newSeatsList);
        //console.log(this.managerAllSeatMap.size + "managerAllSeatMap 처음의 자리 맵 사이즈입니다");
        return this.managerAllSeatMap.get(floor);
      } else {
        //console.log(this.managerAllSeatMap.size + "managerAllSeatMap 현재 자리 맵 사이즈입니다" );
        return this.managerAllSeatMap.get(floor);
      }
    },
    getEachEmployeeSeatList: function (employee_id) {
      if (!this.eachEmployeeSeatMap.get(employee_id)) {
        let newEmployeeSeatList = [];
        this.eachEmployeeSeatMap.set(employee_id, newEmployeeSeatList);
        return this.eachEmployeeSeatMap.get(employee_id);
      } else {
        return this.eachEmployeeSeatMap.get(employee_id);
      }
    },
    // 해당 층의 도형 리스트의 Delete field 전체 true 만들기
    deleteManagerEachFloorSeatList: function (floor) {
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(floor);
      //console.log(managerEachFloorSeatList);
      for (let i = 0; i < managerEachFloorSeatList.length; i++) {
        managerEachFloorSeatList[i].set("delete", true);
      }
    },
    //사원의 자리리스트에서 삭제된 자리를 삭제하기
    deleteEachEmployeeSeatList: function (groupToObject) {
      if (groupToObject.employee_id != null) {
        let oneEmployeeSeatList = this.getEachEmployeeSeatList(
          groupToObject.employee_id
        );

        if (oneEmployeeSeatList) {
          for (let i = 0; i < oneEmployeeSeatList.length; i++) {
            //console.log(typeof oneEmployeeSeatList[i].seatId); //String
            //console.log(typeof groupToObject.seatId); //String
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
            console.log("ctrl true");
            break;
          case 37: // left
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("left", this.floorCanvas.getActiveObject().left - 5);
              this.floorCanvas.getActiveObject().set("modify", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 39: // right
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("left", this.floorCanvas.getActiveObject().left + 5);
              this.floorCanvas.getActiveObject().set("modify", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 38: // up
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("top", this.floorCanvas.getActiveObject().top - 5);
              this.floorCanvas.getActiveObject().set("modify", true);
              this.floorCanvas.renderAll();
            }
            break;
          case 40: // down
            if (this.floorCanvas.getActiveObject()) {
              this.floorCanvas
                .getActiveObject()
                .set("top", this.floorCanvas.getActiveObject().top + 5);
              this.floorCanvas.getActiveObject().set("modify", true);
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
            console.log("ctrl false");
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
        //console.log(typeof department);//String
        //console.log(typeof "부서1");//String
        if (department === "부서1") return Colors.Orange;
        else if (department === "부서2") return Colors.Yellow;
        else if (department === "부서3") return Colors.Green;
      }
    },

    addVacantSeat(mouseDownX, mouseDownY, mouseUpX, mouseUpY) {
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
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
        floor_id: this.currentSelectedFloorId,
        employee_name: null,
        employee_department: null,
        employee_number: null,
        employee_id: null,
        left: mouseDownX,
        top: mouseDownY,
        angle: 0,
        create: true, //생성
        modify: false, //변경
        delete: false, //삭제
      });

      if (this.currentSelectedFloorName != null) {
        if (
          this.getManagerEachFloorSeatList(this.currentSelectedFloorId).length
        ) {
          let seatNumberArray = [];
          this.getManagerEachFloorSeatList(this.currentSelectedFloorId).forEach(
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

      this.floorCanvas.add(group);
      this.floorCanvas.renderAll();
      this.floorCanvas.setActiveObject(group);

      eachFloorSeatList.push(group);
      managerEachFloorSeatList.push(group);

      eventBus.$emit("allSeatMap", this.allSeatMap);
    },
    setMappingSeat(item) {
      if (!this.floorCanvas.getActiveObject()) {
        alert("선택된 좌석이 없습니다.");
        return;
      }

      let mappedOtherEmployeeSeatNameList = [];
      this.floorCanvas.getActiveObjects().forEach((obj) => {
        if (obj.employee_id && obj.employee_id != item.employee_id) {
          mappedOtherEmployeeSeatNameList.push(obj.seatName);
        }
      });

      mappedOtherEmployeeSeatNameList.forEach((obj) => {
        console.log(obj);
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

      let eachEmployeeSeatList = this.getEachEmployeeSeatList(item.employee_id);

      this.floorCanvas.getActiveObjects().forEach((obj) => {
        if (obj.employee_id && obj.employee_id != item.employee_id) {
          // 다른 사원이 매핑된 좌석
          let groupToObject = obj.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
        }
        // 다른 사원이 매핑된 좌석 + 공석
        obj.employee_name = item.name;
        obj.employee_department = item.department;
        obj.employee_number = item.number;
        obj.employee_id = item.employee_id;
        obj.item(0).set("fill", this.getColor(obj.employee_department));
        obj.set("modify", true);
        eachEmployeeSeatList.push(obj);
      });

      this.checkZoom();
      this.floorCanvas.renderAll();
      eventBus.$emit("allSeatMap", this.allSeatMap);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    clickdeleteAllBtn() {
      if (confirm("Are you sure?")) {
        this.floorCanvas
          .getObjects()
          .slice()
          .forEach((obj) => {
            let groupToObject = obj.toObject(["seatId", "employee_id"]);
            this.deleteEachEmployeeSeatList(groupToObject);
            this.floorCanvas.remove(obj);
          });

        this.getEachFloorSeatList(this.currentSelectedFloorId).length = 0;
        this.deleteManagerEachFloorSeatList(this.currentSelectedFloorId);

        eventBus.$emit(
          "eachFloorSeatList",
          this.getEachFloorSeatList(this.currentSelectedFloorId)
        );
        eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
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
          obj.set("delete", true);
          let groupToObject = obj.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
          console.log(this.eachEmployeeSeatMap);
          console.log(eachFloorSeatList);

          let index = eachFloorSeatList.indexOf(obj);
          eachFloorSeatList.splice(index, 1);
          console.log(eachFloorSeatList);
          this.floorCanvas.remove(obj);

          eventBus.$emit("eachFloorSeatList", eachFloorSeatList);
          eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
        });
      } else {
        return;
      }
    },
    changeToVacant() {
      if (confirm("자리를 비우시겠습니까?")) {
        this.floorCanvas.getActiveObjects().forEach((obj) => {
          let groupToObject = obj.toObject(["seatId", "employee_id"]);
          this.deleteEachEmployeeSeatList(groupToObject);
          obj.set("modify", true);
          obj.set("employee_name", null);
          obj.set("employee_department", null);
          obj.set("employee_number", null);
          obj.set("employee_id", null);
          obj.item(0).set("fill", this.getColor(obj.employee_department));
          this.floorCanvas.remove(obj.item(1)); // delete textObject
          obj.item(1).set("text", "");
        });

        this.floorCanvas.renderAll();
        eventBus.$emit("allSeatMap", this.allSeatMap);
        eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
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
      let managerEachFloorList = this.getManagerEachFloorSeatList(
        this.currentSelectedFloorId
      );
      let eachEmployeeSeatList = this.getEachEmployeeSeatList(
        this.floorCanvas.getActiveObject().employee_id
      );

      this.clipboard.clone((clonedObj) => {
        this.floorCanvas.discardActiveObject();
        clonedObj.set({
          left: clonedObj.left + 10,
          top: clonedObj.top + 10,
          create: true,
          delete: false,
          modify: false,
          seatId: this.createSeatUUID(),
          floor_id: this.currentSelectedFloorId,
          angle: activeObject.angle,
          employee_department: activeObject.employee_department,
          employee_id: activeObject.employee_id,
          employee_name: activeObject.employee_name,
          employee_number: activeObject.employee_number,
          evented: true,
        });

        let seatNumberArray = [];
        this.getManagerEachFloorSeatList(this.currentSelectedFloorId).forEach(
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
        managerEachFloorList.push(clonedObj);
        eachEmployeeSeatList.push(clonedObj);
      });

      eventBus.$emit("allSeatMap", this.allSeatMap);
      eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
    },
    showContextMenu(clientX, clientY) {
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
      employee_id,
      employee_name,
      employee_department,
      employee_number
    ) {
      this.toolTipXLocation = clientX;
      this.toolTipYLocation = clientY;

      if (employee_id == null) {
        this.toolTipText = "공석";
        this.toolTipColor = null;
      } else {
        this.toolTipText =
          "이름 : " +
          employee_name +
          "<br>아이디 : " +
          employee_id +
          "<br>부서 : " +
          employee_department +
          "<br>내선번호 : " +
          employee_number;

        this.toolTipColor = "pink lighten-2";
      }

      this.toolTipStatus = true;
    },
    // 층간이동
    changeFloorSeat(floor_id) {
      if (!this.floorCanvas.getActiveObject()) {
        alert("이동할 좌석이 선택되지 않았습니다.");
        return;
      }
      let eachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      let managerEachFloorSeatList = this.getEachFloorSeatList(
        this.currentSelectedFloorId
      );

      for (let i = 0; i < this.allFloorList.length; i++) {
        //console.log(typeof this.allFloorList[i].floor_id) //String
        //console.log(typeof floor_id) //String
        if (this.allFloorList[i].floor_id === floor_id) {
          this.floorCanvas.getActiveObjects().forEach((obj) => {
            obj.set("floor_id", this.allFloorList[i].floor_id);
            obj.set("floor_name", this.allFloorList[i].floor_name);
            obj.set("modify", true);

            let changeFloorSeatList = this.getEachFloorSeatList(
              this.allFloorList[i].floor_id
            );
            let changeManagerFloorSeatList = this.getManagerEachFloorSeatList(
              this.allFloorList[i].floor_id
            );

            changeFloorSeatList.push(obj);
            changeManagerFloorSeatList.push(obj);

            //이동 후에 원래 list에서 삭제
            for (let j = 0; j < eachFloorSeatList.length; j++) {
              //console.log(typeof eachFloorSeatList[j].seatId);//String
              //console.log(typeof obj.seatId);//String
              if (eachFloorSeatList[j].seatId === obj.seatId) { //String
                eachFloorSeatList.splice(j, 1);
              }
            }
            for (let j = 0; j < managerEachFloorSeatList.length; j++) {
              //console.log(typeof managerEachFloorSeatList[j].seatId);//String
              //console.log(typeof obj.seatId);//String
              if (managerEachFloorSeatList[j].seatId === obj.seatId) { //String
                managerEachFloorSeatList[j].set("delete", true);
              }
            }
            eventBus.$emit("showSeatFloor", this.allFloorList[i].floor_id);
            eventBus.$emit("eachFloorSeatList", changeFloorSeatList);
            eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
          });
          this.floorCanvas.renderAll();
        }
      }
    },
    showSeat(seat) {
      //좌석 하이라이트
      let seatFloor = null;
      //seat의 층과 현재층이 같지 않다면
      if (this.currentSelectedFloorId != seat.floorid) {
        //탭 전환 코드
        seatFloor = seat.floorid;
      }
      //seat의 층과 현재층이 같다면
      else {
        seatFloor = this.currentSelectedFloorId;
      }

      let eachFloorSeatList = this.getEachFloorSeatList(seatFloor);
      for (let i = 0; i < eachFloorSeatList.length; i++) {
        let group = eachFloorSeatList[i];
        let asObject = group.toObject([
          "employee_id",
          "floor_id",
          "seatId",
          "employee_department",
        ]);

        let objectSeatId = asObject.seatId;
        //console.log(typeof objectSeatId)//String
        //console.log(typeof asObject.seatId)//String
        if (seat.seatid === objectSeatId) { //String
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
            );
            if (typeof getImageTypeForTypeCheck === "string") {
              //url
              this.loadImageUrl(
                this.allImageMap.get(this.currentSelectedFloorId)
              );
            } else {
              //file
              this.loadImageFile(
                this.allImageMap.get(this.currentSelectedFloorId)
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

          let color = this.getColor(asObject.employee_department);
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
      //csv 내려받기 //seatName, employeeid, floorid
      //for (let i = 0; i < this.managerFloorList.length; i++) {
      let floorid = this.currentSelectedFloorId;
      this.$emit("downloadCSVFile", floorid);
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
      console.log(newFileForCSVType);

      let floorid = this.currentSelectedFloorId;
      let csvFileData = new FormData();
      csvFileData.append("file", newFileForCSVType);

      this.$emit("saveFromCSVFileToDB", csvFileData, floorid);
    },

    clickSaveBtn() {
      this.floorCanvas.discardActiveObject();

      if (this.managerFloorList) {
        //층 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          if (!this.managerFloorList[i].create) {
            // 원본
            if (this.managerFloorList[i].delete) {
              // 001 011 delete
              let deleteFloorKey = this.managerFloorList[i].floor_id;
              this.$emit("deleteFloorWithKey", "floors", deleteFloorKey);
            } else if (this.managerFloorList[i].modify) {
              //010 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveFloors", "floors", floorData);
            }
          } else {
            // front에서 생성
            if (this.managerFloorList[i].delete) {
              //101 111 nothing
              continue;
            } else {
              //100 110 그 id에 대하여 post
              let floorData = {};
              floorData.floor_id = this.managerFloorList[i].floor_id;
              floorData.floor_name = this.managerFloorList[i].floor_name;
              floorData.building_id = this.managerFloorList[i].building_id;
              floorData.floor_order = this.managerFloorList[i].floor_order;

              this.$emit("saveFloors", "floors", floorData);
            }
          }
        }

        //이미지 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          let imgData = new FormData();
          let floorid = this.managerFloorList[i].floor_id;

          let file = this.allImageMap.get(floorid);

          if (file != null) {
            if (typeof file === "string") {
              //url
            } else {
              //file
              imgData.append("imageFile", file);
              this.$emit("saveImages", "images", imgData, floorid);
            }
          }
        }

        //자리 저장
        for (let i = 0; i < this.managerFloorList.length; i++) {
          let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
            this.managerFloorList[i].floor_id
          );

          if (managerEachFloorSeatList.length > 0) {
            //console.log(
            //  managerEachFloorSeatList.length +
            //    this.managerFloorList[i].floor_id +
            //    "층의 자리 개수입니다."
            //);

            for (let j = 0; j < managerEachFloorSeatList.length; j++) {
              let groupToObject = managerEachFloorSeatList[j].toObject([
                "seatId",
                "seatName",
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

              //console.log(groupToObject);
              //axios api 호출
              if (!groupToObject.create) {
                // 원본
                if (groupToObject.delete) {
                  // 001 011 delete
                  let deleteSeatKey = groupToObject.seatId;
                  let deleteSeatFloor = groupToObject.floor_id;
                  this.$emit(
                    "deleteSeatWithKey",
                    "seats",
                    deleteSeatKey,
                    deleteSeatFloor
                  );
                } else if (groupToObject.modify) {
                  //010 그 id에 대하여 post
                  let seatData = {};
                  seatData.seat_id = groupToObject.seatId;
                  seatData.seat_name = groupToObject.seatName;
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

                  this.$emit("saveSeats", "seats", seatData, seatData.floor);
                }
              } else {
                // front에서 생성
                if (groupToObject.delete) {
                  //101 111 nothing
                  continue;
                } else {
                  //100 110 그 id에 대하여 post
                  let seatData = {};
                  seatData.seat_id = groupToObject.seatId;
                  seatData.seat_name = groupToObject.seatName;
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

                  //console.log(seatData);
                  this.$emit("saveSeats", "seats", seatData, seatData.floor);
                }
              }
            }
          }
        }
      }
    },
    getEmployeeObject(employee_id) {
      // seat table의 employee_id를 받으면 그에 맞는 정보 알아오기 위함
      let employeeInfoList = [];
      let employeeObject = {}; // return 될 Object
      for (let i = 0; i < this.employees.length; i++) {
        let employee = {};
        employee.name = this.employees[i].name;
        employee.department = this.employees[i].department;
        employee.number = this.employees[i].number;
        employee.employee_id = this.employees[i].employee_id;

        employeeInfoList.push(employee);
      }

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
          //console.log(typeof employee_id)//String
          //console.log(typeof employeeInfoList[i].employee_id)//String
          if (employee_id === employeeInfoList[i].employee_id) { //String
            employeeObject = employeeInfoList[i];
          }
        }
      }
      //console.log(employeeObject);
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

      let textObject = new fabric.IText("", {
        left: 0,
        top: rectangle.height / 3,
        fontSize: 0,
        fill: "black",
      });

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

      let seatNameObject = new fabric.IText(seat.seat_name, {
        left: rectangle.left,
        top: rectangle.top - 15,
        fontSize: this.fontSize / this.zoom,
        fill: "black",
      });

      group.seatName = seat.seat_name;
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
          let imgurl = this.latestFloorImageFromDb[i].url;
          let floorid = this.latestFloorImageFromDb[i].floorid;
          this.allImageMap.set(floorid, imgurl);
          //console.log("현재층 이미지");
          //console.log(this.allImageMap.get(floorid));
          this.currentSelectedFloorId = floorid;

          this.loadImageUrl(imgurl);
          // 현재층 자리 로드

          if (this.latestFloorSeatListFromDb.length) {
            for (let i = 0; i < this.latestFloorSeatListFromDb.length; i++) {
              this.currentSelectedFloorId = this.latestFloorSeatListFromDb[
                i
              ].floor;

              let eachFloorSeatList = this.getEachFloorSeatList(
                this.latestFloorSeatListFromDb[i].floor
              );
              let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
                this.latestFloorSeatListFromDb[i].floor
              );

              let group = this.makeGroupInfo(this.latestFloorSeatListFromDb[i]);

              this.floorCanvas.add(group);

              eachFloorSeatList.push(group);
              managerEachFloorSeatList.push(group);

              eventBus.$emit("allSeatMap", this.allSeatMap);

              if (this.latestFloorSeatListFromDb[i].employee_id != null) {
                let eachEmployeeSeatList = this.getEachEmployeeSeatList(
                  this.latestFloorSeatListFromDb[i].employee_id
                );
                eachEmployeeSeatList.push(group);
                eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
              }
            }
          } else {
            this.currentSelectedFloorId = floorid;
          }
        }
        this.clickLoadOtherFloors();
      }
    },
    clickLoadOtherFloors() {
      if (this.otherFloorSeatListFromDb) {
        //다른 층 이미지 로드
        for (let i = 0; i < this.otherFloorImageFromDb.length; i++) {
          let imgurl = this.otherFloorImageFromDb[i].url;
          let floorid = this.otherFloorImageFromDb[i].floorid;
          this.allImageMap.set(floorid, imgurl);

          //console.log("다른층 이미지 ");
          //console.log(this.allImageMap.get(floorid));
        }
        //다른 층 자리 로드
        if (this.otherFloorSeatListFromDb) {
          let keys = [];
          keys = Array.from(this.otherFloorSeatListFromDb.keys());

          for (let i = 0; i < keys.length; i++) {
            let seats = [];
            seats = this.otherFloorSeatListFromDb.get(keys[i]);

            for (let j = 0; j < seats.length; j++) {
              let eachFloorSeatList = this.getEachFloorSeatList(seats[j].floor);
              let managerEachFloorSeatList = this.getManagerEachFloorSeatList(
                seats[j].floor
              );

              let group = this.makeGroupInfo(seats[j]);

              eachFloorSeatList.push(group);
              managerEachFloorSeatList.push(group);

              if (seats[j].employee_id != null) {
                let eachEmployeeSeatList = this.getEachEmployeeSeatList(
                  seats[j].employee_id
                );
                eachEmployeeSeatList.push(group);
                eventBus.$emit("eachEmployeeSeatMap", this.eachEmployeeSeatMap);
              }

              eventBus.$emit("allSeatMap", this.allSeatMap);
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
