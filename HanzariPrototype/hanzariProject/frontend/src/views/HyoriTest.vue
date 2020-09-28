<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <div class="search" id="search">
        <AllFloorsDataTable></AllFloorsDataTable>
        <EachEmployeeSeatDataTable></EachEmployeeSeatDataTable>
      </div>
    </div>

    <div class="d3" id="hr"></div>
    
    <div class="d2" id="d2">
      <FloorTabs></FloorTabs>
    </div>

    <div class="d3" id="hr"></div>
    <div class="d4" id="d4">
      <div class="listview" id="listview">
        <EachFloorDataTable></EachFloorDataTable>
      </div>
    </div>

  </div>
</template>

<script>
import AllFloorsDataTable from "@/components/AllFloorsDataTable.vue";
import FloorTabs from "@/components/FloorTabs.vue";
import EachFloorDataTable from "@/components/EachFloorDataTable.vue";
import EachEmployeeSeatDataTable from "@/components/EachEmployeeSeatDataTable.vue";

export default {
  name: "HyoriTest",
  components: {
    AllFloorsDataTable,
    FloorTabs,
    EachFloorDataTable,
    EachEmployeeSeatDataTable
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
  },
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