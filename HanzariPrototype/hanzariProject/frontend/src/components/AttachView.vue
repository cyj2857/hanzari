<template>
  <div>
    <canvas ref="canvas" class="canvas" width="1100px" height="800px"></canvas>
    <input v-show="false" ref="inputUpload" type="file" @change="onFileChange" />
    <v-btn color="success" @click="$refs.inputUpload.click()">File upload to background</v-btn>
    <v-btn @click="showSvgBtn" class="SvgBtn">canvas to svg (check in console log)</v-btn>
    <v-btn @click="deleteAllBtn">delete shapes on canvas</v-btn>
    <v-btn @click="clickSaveBtn">Save Canvas</v-btn>
    <v-btn @click="deleteBtn">delete selected shape</v-btn>
  </div>
</template>

<script>
import { eventBus } from "../main.js";
import axios from 'axios';

export default {
  data: function() {
    return {
      myCanvas: null,
      mySeatList: null,
      myImageList: null,
      seatId: 0,
    }
  },
  created() {
    eventBus.$on("createdRect", item => {
      this.makeRectBtn(item)
    }),
    eventBus.$on("changeFloor", floor =>{
      this.changeFloor(floor+"floor")
    })
  },
  mounted(){
    this.initializing();
  },
  methods: {
    changeFloor(floor){
      console.log(floor)
      this.initializing()
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });
      this.myCanvas.backgroundImage = 0;
      this.myCanvas.backgroundColor = 'aliceblue';
      this.myCanvas.renderAll();
    },
    initializing() {
      if (this.myCanvas == null) {
        const ref = this.$refs.canvas;
        this.myCanvas = new fabric.Canvas(ref)
      }
      if (this.mySeatList == null) {
        this.mySeatList = new Array()
      }
      if (this.myImageList == null) {
        this.myImageList = new Map()
      }
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files
      if (!files.length) return
      this.createImage(files[0])
    },
    createImage(file) {
      this.initializing();
      var reader = new FileReader()
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
      reader.readAsDataURL(file)
      this.saveImage(file)
    },
    saveImage(file){
      this.myImageList.set(this.newFloorNum, file)
      console.log(this.myImageList.get(this.newFloorNum))
    },
    makeRectBtn(item) {
      this.initializing()
      var rectangle = new fabric.Rect({
        width: 50,
        height: 50,
        fill: "blue",
        opacity: 1
      })
      var textObject = new fabric.IText(item.name, {
        left: 0,
        top: 0,
        fontSize: 13,
        fill: "#000000"
      })
      var group = new fabric.Group([rectangle, textObject], {
        id: item.employee_id,
        seatId : this.seatId ++, // 1,2,3,4
        employee_id: item.employee_id,
        left: 150,
        top: 150
      })
      //db- getId
      //group.toObject(['seat_id'])=akfjkdsk 
      group.on("mouseover", function(e) {
        var group = e.target;
        group.item(0).set("fill", "red");

        var asObject = group.toObject(['employee_id'])
        var x = group.toObject(['left'])
        console.log(asObject.employee_id)//1771354
        console.log("hi"+x.left);//150
      })
      var asObject = group.toObject(['seatId'])
      console.log(asObject.seatId)
      //console.log(group.item(0))
      //console.log(group.item(1))

      this.myCanvas.add(group);

      //this.mySeatArray.push(group)
      //console.log(this.mySeatArray[0].item(1))
    },
    deleteAllBtn() {
      this.initializing();
      this.myCanvas
        .getObjects()
        .slice()
        .forEach(obj => {
          this.myCanvas.remove(obj);
        });
    },
    deleteBtn () {
      this.initializing();
      var activeObject = this.myCanvas.getActiveObject()
      if (activeObject) {
          if (confirm('Are you sure?')) {
              this.myCanvas.remove(activeObject);
          }
      }
    },
    showSvgBtn() {
      this.initializing();
      console.log("svg : " + this.myCanvas.toSVG());
      //logs the SVG representation of canvas
    },
    clickSaveBtn() {
      this.initializing();
      this.$axios.post('/springBootURL/',{})//?��중에 층마?�� ????��?�� ?��?��?�� URL?��?�� �? ?��?��?��주기
      .then((response) => {
        this.result=response.data
      })
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
