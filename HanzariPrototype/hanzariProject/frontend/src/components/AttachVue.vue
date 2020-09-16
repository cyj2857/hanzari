<template>
    <div>
        <v-btn @click="updateText">{{ changeText }}</v-btn>
    </div>
</template>

<script>
export default {
  data () {
    return{      
        changeText:'Sample Text'
        }
    },
    methods: {
        updateText() {
        this.changeText = 'Click Event Test'
    }
  }
}
</script>

<style scoped>

</style>
