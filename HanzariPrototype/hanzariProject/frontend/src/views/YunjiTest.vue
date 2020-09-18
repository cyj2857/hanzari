<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <select v-model="selected">
        <option value disabled hidden>Please Choose Floor</option>
        <option>1floor</option>
        <option>2floor</option>
        <option>3floor</option>
      </select>
      <MyCanvas></MyCanvas>

      <div class="search" id="search">
        <input type="text" placeholder="Input Text" />
        <button>Search</button>
        <ListView></ListView>
      </div>
    </div>

    <div class="d3" id="hr"></div>

    <div class="d2" id="d2">
      <AttachVue></AttachVue>
    </div>
  </div>
</template>

<script>
import MyCanvas from "@/components/MyCanvas.vue";
import ListView from "@/components/ListView.vue";
import AttachVue from "@/components/AttachView.vue";
export default {
  name: "YunjiTest",
  components: {
    MyCanvas,
    ListView,
    AttachVue
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
      msg: "This is yunji page.",
      floorMsg: "Choose Floor",
      searchEmployeeMsg: "Search Employee",
      selected: ""
    };
  },
  methods: {
    on_mouse_down(event) {
      this.startpos = event.clientX + this.diffpos;
      this.isEnable = true;
      return false;
    },
    on_mouse_up(event) {
      this.isEnable = false;
    },
    on_mouse_move(event) {
      if (this.isEnable) {
        pos = event.clientX;
        this.diffpos = this.startpos - pos;
        width = window.innerWidth / 2;
        if (
          this.diffpos > -(width - this.range) &&
          this.diffpos < width - this.range
        ) {
          document.getElementById("d1").style.width =
            width - this.diffpos + "px";
          document.getElementById("d2").style.width =
            width - 20 + this.diffpos + "px";
        }
      }
    }
  },
  mounted() {
    document.getElementById("hr").onmousedown = this.on_mouse_down();
    document.onmouseup = this.on_mouse_up();
    document.onmousemove = this.on_mouse_move();
  }
};
</script>

<style scoped>
.d1 {
  float: left;
  width: 20%;
  height: 100%;
  border-right: 1px solid #b8b8b8;
  margin-right: -1px;
  overflow-y: scroll;
}

.d2 {
  float: left;
  width: 75%;
  height: 100%;
  overflow-y: scroll;
}

.d3 {
  float: left;
  width: 5%;
  height: 100%;
  background-color: #888888;
  resize: horizontal;
}

#hr {
  cursor: pointer;
}

div {
  display: inline-block;
}
</style>
