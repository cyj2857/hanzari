<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <!-- <select v-model="selected">
          <option value="" disabled hidden>Please Choose Floor</option>
          <option>1floor</option>
          <option>2floor</option>
          <option>3floor</option>
        </select>
      <MyCanvas></MyCanvas> -->

      <div class="search" id="search">
        <MyDataTable></MyDataTable>
      </div>
    </div>

    <div class="d3" id="hr"></div>
    
    <div class="d2" id="d2">
      <AttachView></AttachView>
    </div>

    <div class="d3" id="hr"></div>
    <div class="d4" id="d4">
      <div class="listview" id="listview">
        <FloorDataTable></FloorDataTable>
      </div>
    </div>

  </div>
</template>

<script>
import MyCanvas from "@/components/MyCanvas.vue";
import ListView from "@/components/ListView.vue";
import MyDataTable from "@/components/MyDataTable.vue";
import FloorDataTable from "@/components/FloorDataTable.vue";
import AttachView from "@/components/AttachView.vue";

export default {
  name: "HyoriTest",
  components: {
    MyCanvas,
    ListView,
    AttachView,
    MyDataTable,
    FloorDataTable
  },
  props: {
    startpos: {
      type: Number,
      default: 0
    },
    diffpos: {
      type: Number,
      default: 0
    },
    range: {
      type: Number,
      default: 50
    },
    isEnable: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      msg: "This is hyori page.",
      floorMsg: "Choose Floor",
      searchEmployeeMsg: "Search Employee",
      changeText: "Sample Text",
      selected: ""
    };
  },
  methods: {
    updateText() {
      this.changeText = "Click Event Test";
    },
    on_mouse_down(e) {
      startpos = event.clientX + diffpos;
      isEnable = true;
      return false;
    },
    on_mouse_up(e) {
      isEnable = false;
      return false;
    },
    on_mouse_move(e) {
      if (isEnable) {
        pos = event.clientX;

        diffpos = startpos - pos;

        var width = window.innerWidth / 2;
        if (diffpos > -(width - range) && diffpos < width - range) {
          document.getElementById("d1").style.width = width - diffpos + "px";
          document.getElementById("d2").style.width =
            width - 20 + diffpos + "px";
        }
      }
    }
  },
  created() {
    document.getElementById("hr").onmousedown = on_mouse_down;
    document.onmouseup = on_mouse_up;
    document.onmousemove = on_mouse_move;
  }
};
</script>

<style scoped>
.d1 {
  float: left;
  width: 23%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
  overflow-y: scroll;
}

.d2 {
  float: left;
  width: 53%;
  height: 100%;
  overflow-y: scroll;
}

.d3 {
  float: left;
  width: 0.3%;
  height: 100%;
  background-color: #888888;
}

.d4 {
  float: left;
  width: 23%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
  overflow-y: scroll;
}


#hr {
  cursor: pointer;
}

div {
  display: inline-block;
}
</style>