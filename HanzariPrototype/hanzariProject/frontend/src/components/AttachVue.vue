<template>
  <div>
    <canvas width="100%" height="60%" class="canvas"></canvas>
    <div class="text-center">
      <input ref="imageInput" type="file" hidden @change= "onChangeImages">
      <v-btn class="ma-2" tile outlined color="success" @click="clickImport">
        {{ importText }}
      </v-btn>
      <v-img v-if="imageUrl" :src="imageUrl"></v-img>
      
      <v-btn class="ma-2" tile color="indigo" dark @click="clickSave">
        {{ saveText }}
      </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      importText: '불러오기',
      saveText: '저장',
      imageUrl: null
    }
  },
  methods: {
    clickImport () {
      this.$refs.imageInput.click();
    },
    onChangeImages (e) {
      console.log(e.target.files)
      const file = e.target.files[0]
      this.imageUrl = URL.createObjectURL(file)
    },
    clickSave () {
      this.saveText = '저장완료'
    },
    drawImage () {
      
    }
  },
  mounted () { //mounted 이후부터 canvas의 dom에 접근 가능
    this.ctx = this.$el.getContext('2d')
  }
}
</script>

<style scoped>
.canvas {
  border: 1px solid #000;
  background: aliceblue;
}
</style>
