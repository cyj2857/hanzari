<template>
  <v-card flat color="transparent">
    <v-icon large style="float: right" @click="changeBackPage">clear</v-icon>
    <v-card-title>
      <v-text-field
        v-model="search"
        append-icon="mdi-magnify"
        label="이름/부서/내선번호 중 입력하세요. 예) 마효리, 부서1, 111-0001"
        single-line
        hide-details
      ></v-text-field>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="this.employees"
      :search="search"
      height="400px"
      class="elevation-1"
      no-data-text="데이터가 없습니다"
      :footer-props="{
        'items-per-page-text': '페이지 당 사원수',
        'items-per-page-options': [5, 10],
      }"

    >
      <template v-slot:item="row">
        <tr>
          <td>{{ row.item.name }}</td>
          <td>{{ row.item.department }}</td>
          <td>{{ row.item.number }}</td>
          <td>
            <v-icon
              large
              id="mappingEmployeeToVacantButton"
              @click="clickEmployeeToMapping(row.item)"
              >add_box</v-icon
            >
          </td>
        </tr>
      </template>
      <template v-slot:pageText="props">
        ITEMS {{ props.pageStart }} - {{ props.pageStop }} OF
        {{ props.itemsLength }}
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
import { eventBus } from "../main";
export default {
  name: "MappingEmployee",
  props: ["copyFromManageSeatsEmployeeList"],
  data() {
    return {
      employees: this.copyFromManageSeatsEmployeeList,
      search: "",
      headers: [
        { text: "이름", align: "start", sortable: true, value: "name" },
        { text: "부서", value: "department" },
        { text: "내선번호", value: "number" },
        { text: "", value: "mappingEmployeeToVacantButton" },
      ],
      allEmployeeSeatMap: null,
    };
  },
  created() {
    eventBus.$on("pushEachEmployeeSeatMap", (eachEmployeeSeatMap) => {
      this.allEmployeeSeatMap = eachEmployeeSeatMap;
    });
  },
  beforeDestroy() {
    eventBus.$off("pushEachEmployeeSeatMap");
  },
  methods: {
    clickEmployeeToMapping(item) {
      eventBus.$emit("mappingEmployeeToVacant", item);
    },
    changeBackPage() {
      eventBus.$emit("pushMappingEmployeeComponentStatus", false);
    },
  },
};
</script>