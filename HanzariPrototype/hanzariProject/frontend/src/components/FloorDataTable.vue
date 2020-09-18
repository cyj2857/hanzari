<template>
  <v-card>
    <v-card-title>
      1Floor
      <v-spacer></v-spacer>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="employees"
    ></v-data-table>
  </v-card>
</template>

<script>
  export default {
    data () {
      return {
        headers: [
          {
            text: 'Name',
            align: 'start',
            sortable: true,
            value: 'name',
          },
          { text: 'Department', value: 'department' },
          { text: 'Number', value: 'number' }
        ],
        employees: [
          {
            name: 'Ma Hyori',
            department: 'Development Team',
            number: '010-5617-4977'
		  },
		  {
            name: 'No Yunji',
            department: 'Development Team',
            number: '010-4673-2827'
		  },
		  {
            name: 'Chae Yujin',
            department: 'Development Team',
            number: '010-7906-3827'
          },
        ],
      }
    },
  }
</script>