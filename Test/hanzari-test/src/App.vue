<template>
  <!-- 와이어프레임(뼈대)만 적기 -->
  <v-app id="app">
      <TopContainer></TopContainer>
      <LeftContainer></LeftContainer>
      <RightContainer></RightContainer>
      <MainContainer></MainContainer>
      <FooterButtons></FooterButtons>
  </v-app>
</template>

<script>
//components 폴더에 있는 vue 파일들 불러와서 template 태그 안의 해당 부분들에 넣기
import FooterButtons from "@/components/FooterButtons.vue";
import LeftContainer from "@/components/LeftContainer.vue";
import MainContainer from "@/components/MainContainer.vue";
import RightContainer from "@/components/RightContainer.vue";
import TopContainer from "@/components/TopContainer.vue";
export default {
  components: {
    FooterButtons,
    LeftContainer,
    MainContainer,
    RightContainer,
    TopContainer,
  },
  data: () => ({ drawer: null }),
};
</script>