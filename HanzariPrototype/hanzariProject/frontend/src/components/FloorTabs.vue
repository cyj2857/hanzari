<template>
  <v-card>
    <v-card-text class="text-center">
    <AttachView></AttachView>
      <v-btn text @click="length--">Remove Tab</v-btn>
      <v-divider class="mx-4" vertical></v-divider>
      <v-btn text @click="length++">Add Tab</v-btn>
    </v-card-text>
    
    <v-tabs
      v-model="tab"
      background-color="cyan"
      dark
    >
      <v-tab
        v-for="n in length"
        :key="n"
      >
        {{ n }} Floor
      </v-tab>
    </v-tabs>
    
  </v-card>
</template>

<script>
import AttachView from "@/components/AttachView.vue";
export default {
    components: {
    AttachView,
     },
    data: () => ({
        length: 3,
        tab: null,
    }),
    
    watch: {
      length (val) {
        this.tab = val - 1
      },
    },
  }
</script>