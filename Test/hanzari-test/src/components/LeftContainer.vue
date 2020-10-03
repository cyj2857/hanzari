<template>
  <v-navigation-drawer v-model="drawer" app color="grey lighten-3">
    <v-container>
      <v-row>
        <v-col>
          <v-sheet rounded="lg" min-height="100vh">
              <v-list rounded="lg">
                <v-subheader>My chuseok gone</v-subheader>
                <v-list-item-group v-model="item" color="primary">
                  <v-list-item v-for="(item, i) in items" :key="i">
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
export default {
  data: () => ({
    item: 1,
    items: [
      { text: "이미지 업로드", icon: "mdi-image"},
      { text: "좌석 정보 보기", icon: "mdi-folder-account-outline" },
      { text: "공석 만들기", icon: "mdi-seat" },
      { text: "선택 좌석 삭제", icon: "mdi-account-minus" },
      { text: "전체 좌석 삭제", icon: "mdi-account-multiple-minus" },
    ],
  }),
};
</script>