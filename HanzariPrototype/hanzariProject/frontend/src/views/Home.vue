<template>
    <div>
        <h1>This is Home Page</h1>
        <EssentialLinks />
    </div>
</template>

<script>
import EssentialLinks from '@/components/HelloWorld.vue'

export default {
  name: 'Home',
  components: {
    EssentialLinks
  },

  data () {
    return {
      msg: 'Welcome to Your Vue.js App'
    }
  }
}
</script>
