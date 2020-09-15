<template>
    <canvas width="230" height="300" class="canvas"></canvas>
</template>

<script>
export default {
    props: {
    radius: {
      type: Number,
      default: 50
    }
  },
  watch: {
    radius () {
      this.draw(this.radius)
   }
  },
  methods: {
    draw (radius) {
      this.ctx.beginPath()
      this.ctx.clearRect(0, 0, 200, 200)
      this.ctx.arc(100, 100, radius, 0, Math.PI * 2)
      this.ctx.fill()
    }
  },
  mounted () {
    this.ctx = this.$el.getContext('2d')
    this.draw(this.radius)
  }
}
</script>

<style scoped>
.canvas{
    border: 1px solid #000;
    background-color: aliceblue;
}
</style>
