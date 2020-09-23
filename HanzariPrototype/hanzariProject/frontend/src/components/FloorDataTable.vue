<template>
  <v-card>
    <v-card-title>
      1Floor
      <v-spacer></v-spacer>
    </v-card-title>
    <v-data-table :headers="headers" :items="employees" class="elevation-1">
      <template v-slot : item.department="{ item }">
        <v-chip :color="getColor(item.department)" dark>{{ item.department }}</v-chip>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
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
          name: "Choi Yujin",
          department: "Secure Team",
          number: "010-7906-3827"
		},
		{
          name: "Kim Dongmin",
          department: "Engineering Team",
          number: "010-7906-3827"
		},
		{
          name: "Ahn Hyunho",
          department: "Engineering Team",
          number: "010-7906-3827"
        }
      ]
    };
  },
  methods: {
    getColor(department) {
      if (department=="Secure Team") return "red";
	    else if (department=="Development Team") return "orange";
	    else if (department=="Engineering Team") return "green";
    }
  }
};
</script>