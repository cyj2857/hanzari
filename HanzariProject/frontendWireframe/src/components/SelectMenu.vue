<template>
  <v-selectmenu :data="list" v-model="value">
  </v-selectmenu>
</template>

<script>
  export default {
    data(){
      return {
        value: 'hihi',
        list: [
          { id: 1, name: 'Chicago Bulls', desc: 'hii' },
          { id: 2, name: 'Cleveland Cavaliers', desc: 'joo' },
        ]
      }
    }
  }
</script>