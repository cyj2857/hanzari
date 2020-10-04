<template>
  <v-navigation-drawer v-model="drawer" app color="grey lighten-3">
    <v-container>
      <v-row>
        <v-col>
          <v-sheet rounded min-height="100vh">
            <v-list rounded>
              <v-subheader>한자리</v-subheader>
              <v-list-item-group v-model="item" color="primary">
                <!-- sendEachButtonEvent 메소드로 각각의 버튼에 해당하는 이벤트를 이벤트버스로 다른 컴포넌트에 전달 -->
                <v-list-item
                  v-for="(item, i) in items"
                  :key="i"
                  @click="sendEachButtonEvent(item)"
                >
                  <v-list-item-icon>
                    <v-icon v-text="item.icon"></v-icon>
                  </v-list-item-icon>
                  <v-list-item-content>
                    <v-list-item-title v-text="item.text"></v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list-item-group>
            </v-list>
          </v-sheet>
        </v-col>
      </v-row>
    </v-container>
  </v-navigation-drawer>
</template>

<script>
import EventBus from "@/EventBus.js";
export default {
  data: () => ({
    item: 1,
    //각각의 버튼을 리스트 형식으로 저장 및 고유의 id 부여
    items: [
      { id: 1, text: "이미지 업로드", icon: "mdi-image" },
      { id: 2, text: "좌석 정보 보기", icon: "mdi-folder-account-outline" },
      { id: 3, text: "공석 만들기", icon: "mdi-seat" },
      { id: 4, text: "선택 좌석 삭제", icon: "mdi-account-minus" },
      { id: 5, text: "전체 좌석 삭제", icon: "mdi-account-multiple-minus" },
    ],
    drawer: false,
  }),
  created() {
    //토글이기 때문에 옵션으로 보내 준 값은 없다.
    EventBus.$on("drawerControl", () => {
      this.drawer = !this.drawer;
    });
  },
  methods: {
    //각각의 버튼(v-list-item) 클릭 시 고유 id로 식별 및 해당 이벤트 이벤트 버스로 전달
    sendEachButtonEvent(item) {
      console.log(item.id);
      for (var i = 1; i <= this.items.length; i++) {
        if (item.id == 1) {
          EventBus.$emit("imageUpload");
        } else if (item.id == 2) {
          EventBus.$emit("showSelectedSeatInformation");
        } else if (item.id == 3) {
          EventBus.$emit("makeVacancy");
        } else if (item.id == 4) {
          EventBus.$emit("selectedSeatRemove");
        } else if (item.id == 5) {
          EventBus.$emit("allSeatsRemove");
        }
      }
    },
  },
};
</script>