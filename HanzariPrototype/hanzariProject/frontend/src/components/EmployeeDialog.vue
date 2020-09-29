<template>
  <v-dialog max-width="290" :value="dialog" persistent>
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>{{dialogStatus}}</v-card-title>
        <v-card-text>
        <v-card-title>
          {{this.employee_name}} Employee Information
        </v-card-title>
        <v-card-text>
          floor : {{this.floor_id}} 
          id : {{this.employee_id}}
          name : {{this.employee_name}}
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="$emit('close')">Close</v-btn>
          <v-btn @click="console">test</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
import { eventBus } from '../main'
export default {
  props: {
    dialogStatus: {
        type: Boolean
    }
  },
  data () {
    return {
      employee_id: '',
      floor_id: '',
      employee_name: ''
    }
  },
  computed: {
    dialog () {
      return this.dialogStatus
    }
  },
  created() {
    eventBus.$on('employee_id', employee_id => {
      this.employee_id = employee_id
    });
    eventBus.$on('floor_id', floor_id => {
      this.floor_id = floor_id
    });
    eventBus.$on('employee_name', employee_name => {
      this.employee_name = employee_name
    }); 
    
  },
  methods: {
    console(){
      console.log(this.employee_id)
      console.log(this.floor_id)
      console.log(this.employee_name)
    }
  }
}
</script>
