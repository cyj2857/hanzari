<template>
    <div>
      <div class="buttonDiv">
        <button @click="button1Click" class="figureBtn">
          <img src="../assets/square.png">
        </button>
        <button @click="button2Click" class="figureBtn">
          <img src="../assets/circle.png"></button>
        <button @click="button3Click" class="figureBtn">
          <img src="../assets/triangle.png">
        </button>
        <button @click="button4Click" class="figureBtn">
          <img src="../assets/pentagon.png">
        </button>
      </div>
    </div>    
</template>

<script>
export default {
  data: {
    button1Img: '../assets/square.png'
  },
  methods: {
    button1Click() {
      alert('Button1 Click!')
    },
    button2Click() {
      alert('Button2 Click!')
    },
    button3Click() {
      alert('Button3 Click!')
    },
    button4Click() {
      alert('Button4 Click!')
    }
  }
}
</script>

<style scoped>
.buttonDiv{
  width: 100%;
  height: 100%;
  border: 1px solid #000;
  background-color: aliceblue;
  padding: 30px; 
  display: inline-block;
}

.figureBtn{
  width: 150px;
  height: 100px;
  padding: 30px; 
}

img {
  width:100%;
  height: 100%;
}

</style>
