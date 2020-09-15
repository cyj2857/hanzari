<template>
  <div class="hanzari" id="hanzari">
    <div class="d1" id="d1">
      <div class="select" id="select">
        <h3>
          <label>{{floorMsg}}</label>
        </h3>
        <select v-model="selected">
          <option disabled value="Please Choose Floor">Please choose floor</option>
          <option>1floor</option>
          <option>2floor</option>
          <option>3floor</option>
        </select>
        <!-- <span>Selected: {{ selected }}</span> -->
      </div>

      <canvas id="myCanvas" style="background-color: aliceblue" width="230" height="300"></canvas>

      <h3>
        <label>{{searchEmployeeMsg}}</label>
      </h3>
      <div class="search" id="search">
        <input type="text" placeholder="Input Text" />
        <button>Search</button>
      </div>
    </div>

    <div class="d3" id="hr"></div>
    <div class="d2" id="d2">
      <button @click="updateText">{{ changeText }}</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'HyoriTest',
  data() {
    return {
      msg: "This is Hyori's page.",
      floorMsg: "Choose Floor",
      searchEmployeeMsg: "Search Employee",
      changeText:"Sample Text",
      selected: ""
    };
  },
  methods: {
    updateText() {
      this.changeText = "Click Event Test";
    }
  }
};
</script>