<template>
  <v-main class="grey lighten-3">
    <v-container>
      <v-row align="center">
        <v-col>
          <canvas ref="canvas" class="canvas"></canvas>
        </v-col>
      </v-row>
      <div>
      </div>
      <!-- 버튼 정의 row -->
      <v-row align="center" justify="space-around">
        <!-- 이미지를 불러오기 위해 input 태그 필요 -->
        <input
          v-show="false"
          ref="inputUpload"
          type="file"
          @change="imageUpload"
        />
        <v-btn
          elevation="2"
          color="#FF6E40"
          rounded
          class="white--text"
          @click="$refs.inputUpload.click()"
        >
          <span>이미지 업로드</span>
        </v-btn>
        <v-btn elevation="2" color="#FF6E40" rounded absoloute class="white--text">
          <span>좌석 정보 보기</span>
        </v-btn>
        <v-btn elevation="2" color="#FF6E40" rounded class="white--text">
          <span>공석 만들기</span>
        </v-btn>
        <v-btn elevation="2" color="#FF6E40" rounded class="white--text">
          <span>선택 좌석 삭제</span>
        </v-btn>
        <v-btn elevation="2" color="#FF6E40" rounded class="white--text">
          <span>전체 좌석 삭제</span>
        </v-btn>
        <v-btn
          elevation="2"
          color="#FF6E40"
          rounded
          class="white--text"
          @click="addTab()"
        >
          <span>층 추가</span>
        </v-btn>
        <v-btn
          elevation="2"
          color="#FF6E40"
          rounded
          class="white--text"
          @click="removeTab()"
        >
          <span>층 삭제</span>
        </v-btn>
      </v-row>
    </v-container>
  </v-main>
</template>

<script>
import EventBus from "@/EventBus.js";
import { fabric } from "fabric";
export default {
  data: () => ({
    floorCanvas: null,
    floorImageList: null,
    currentSelectedFloor: null,
    eachFloorSeatMap: null,
    allFloorsSeatMap: null,
    eachEmployeeSeatMap: null,
  }),
  created() {
    EventBus.$on("imageUpload", () => {
      this.imageUpload();
    });
  },
  mounted() {
    this.initializing();
  },
  methods: {
    initializing() {
      if (this.floorCanvas == null) {
        const ref = this.$refs.canvas;
        const canvas = new fabric.Canvas(ref, {
          backgroundColor: "white",
          width: 1620,
          height: 800,
        });
        const rect = new fabric.Rect({
          left: 100,
          top: 100,
          fill: "red",
          width: 20,
          height: 20,
        });
        canvas.add(rect);
      }
      if (this.floorImageList == null) {
        this.floorImageList = new Map();
      }
      if (this.allFloorsSeatMap == null) {
        this.allFloorsSeatMap = new Map();
      }
      if (this.eachFloorSeatMap == null) {
        this.eachFloorSeatMap == new Map();
      }
      if (this.eachEmployeeSeatMap == null) {
        this.eachEmployeeSeatMap == new Map();
      }
    },
    imageUpload() {
      var reader = new FileReader();
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
      //reader.readAsDataURL();
    },
    addTab() {},
    removeTab() {},
  },
};
</script>