<template>
  <v-card>
    <v-card-title>
      <v-spacer></v-spacer>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="Search"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" :search="search"></v-data-table>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      search: "",
      headers: [
        {
          text: "Name",
          align: "start",
          sortable: true,
          value: "name"
        },
        { text: "Department", value: "department" },
        { text: "Number", value: "number" }
      ],
      employees: [
        {
          name: "Ma Hyori",
          department: "Development Team",
          number: "010-5617-4977"
        },
        {
          name: "No Yunji",
          department: "Development Team",
          number: "010-4673-2827"
        },
        {
          name: "Chae Yujin",
          department: "Secure Team",
          number: "010-7906-3827"
        }
      ]
    };
  }
};
</script>