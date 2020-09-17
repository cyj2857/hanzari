<template>
  <div>
    <canvas ref="canvas" width="100%" height="60%" class="canvas"></canvas>
    <div class="text-center">
      <input ref="imageInput" type="file" hidden @change= "onChangeImages">
      <v-btn class="ma-2" tile outlined color="success" @click="clickImport">
        {{ importText }}
      </v-btn>
      <!-- <v-img v-if="imageUrl" :src="imageUrl"></v-img> -->
      
      <v-btn class="ma-2" tile color="indigo" dark @click="clickSave">
        {{ saveText }}
      </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    radius: {
      type: Number,
      default: 50
    },
    imageUrl:{
      type: String,
      default: "https://konvajs.org/assets/yoda.jpg"
    }
  },
  watch: {
  draw () {
      this.draw(this.imageUrl)
   }
  },
  data () {
    return {
      importText: 'import',
      saveText: 'save'
    }
  },
  methods: {
    draw(imageUrl) {
      this.ctx.beginPath()
      this.ctx.clearRect(0, 0, 200, 200)

      var img = new Image()
      img.src = imageUrl

      this.ctx.drawImage(img,0,0)
      this.ctx.fill()
    },
    clickImport () {
      this.$refs.imageInput.click();
    },
    onChangeImages (e) {
      console.log(e.target.files)
      const file = e.target.files[0]
      this.imageUrl = URL.createObjectURL(file)
    },
    clickSave () {
      this.saveText = 'Success Save'
    },
    func () {
      let cvn = this.$refs.canvas
      let ctx = cvn.getContext("2d")
      ctx.beginPath()
      ctx.clearRect(0, 0, 200, 200)

      var img = new Image()
      img.src = this.imageUrl

      ctx.drawImage(img,0,0)
      ctx.fill()

    }
  },
  mounted () {
    this.func()
  }
}
</script>

<style scoped>
.canvas {
  border: 1px solid #000;
  background: aliceblue;
}
</style>