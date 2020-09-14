<template>
  <div class="hello">
    <h1> {{ text }} </h1>
    <button @click="updateText">Change Text</button>
  </div>
</template>

<script>
export default {
  data () {
    return {
      text: 'This is hancom page.'
    }
  },

  methods: {
    updateText () {
      this.text = 'Click Event Test'
    }
  }
}
</script>

<style scoped>

</style>
