<template>
  <v-dialog max-width="290" :value="dialog">
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>test</v-card-title>
        <v-card-text>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="$emit('close')">OK</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
export default {
  props: {
    dialogStatus: {
        type: Boolean
    }
  },
  data () {
    return {
    }
  },
  computed: {
    dialog () {
      return this.dialogStatus
    }
  }
}
</script>
