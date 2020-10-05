<template>
<v-app-bar app color="blue" rounded>
    <v-app-bar-nav-icon
      color="white"
      @click="rightContainerControl"
    ></v-app-bar-nav-icon>
    <v-toolbar-title>
      <span class="white--text">한자리</span>
    </v-toolbar-title>
    <!-- 공간 띄워주기 -->
    <v-spacer></v-spacer>
    <!-- v-btn class는 text color -->
    <v-btn elevation="2" medium class="blue--text">
      <!-- font awesome 적용해서 저장 버튼 변경하기 -->
      <span class="font-weight-bold">저장하기</span>
    </v-btn>
</v-app-bar>
</template>

<script>
import EventBus from "@/EventBus.js";
export default {
  methods:{
    rightContainerControl(){
      //$emit{"이벤트 이름", 옵션}
      EventBus.$emit("drawerControl");
    }
  }
}
</script>