<template>
  <!-- 와이어프레임(뼈대)만 적어두기 -->
  <v-app id="app">
      <TopComponents></TopComponents>
      <LeftComponents></LeftComponents>
      <RightComponents></RightComponents>
      <MainComponents></MainComponents>
      <BottomComponents></BottomComponents>
  </v-app>
</template>

<script>
//components 폴더에 있는 vue 파일들 불러와서 template 태그 안의 해당 부분들에 넣기
import BottomComponents from "@/components/BottomComponents.vue"; 
import LeftComponents from "@/components/LeftComponents.vue";
import MainComponents from "@/components/MainComponents.vue";
import RightComponents from "@/components/RightComponents.vue";
import TopComponents from "@/components/TopComponents.vue";
export default {
  components: {
    BottomComponents,
    LeftComponents,
    MainComponents,
    RightComponents,
    TopComponents,
  }
}
</script>