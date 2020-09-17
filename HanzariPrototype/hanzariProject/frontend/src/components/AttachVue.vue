<template>
  <div>
    <canvas width="100%" height="60%" class="canvas"></canvas>
    <div class="text-center">
      <v-btn class="ma-2" tile color="indigo" dark @click="clickSave">{{ saveText }}</v-btn>
      <v-btn class="ma-2" tile outlined color="success" @click="clickImport"> {{ importText }}</v-btn>
    </div>
  </div>
</template>

<script>
export default {
  data () {
    return {
      saveText: '저장',
      importText: '불러오기'
    }
  },
  methods: {
    clickSave () {
      this.saveText = '저장완료'
    },
    clickImport () {
      this.importText = '불러오기 완료'
    }
  },
  mounted () {
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
