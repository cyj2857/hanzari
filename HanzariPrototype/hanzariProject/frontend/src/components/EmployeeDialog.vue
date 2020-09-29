<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>{{dialogInfo.dialogTitle}}</v-card-title>
        <v-card-text>
          {{dialogInfo.employee_name}}
          {{dialogInfo.employee_department}}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="$emit('close')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
export default {
  props: {
    dialogInfo: {
        type: Object
    }
  },
  data () {
    return {
    }
  },
  computed: {
    dialog () {
      return this.dialogInfo.dialogStatus
    }
  }
}
</script>
