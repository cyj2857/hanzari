<template>
  <v-dialog max-width="290" :value="dialog">
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>{{ dialogTitle }}</v-card-title>
        <v-card-text>
          <v-text-field
            label="세트 이름"
            @keyup.enter=""
            ></v-text-field>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="success" @click="">확인</v-btn>
          <v-btn @click="$emit('close')">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
export default {
  props: {
    setDialog: {
      type: Object
    }
  },
  data () {
    return {

    }
  },
  computed: {
    dialog () {
      return this.setDialog.dialog
    },
    dialogTitle () {
      if (this.setDialog.dialogMode === 'add') {
        return '세트 추가'
      } else if (this.setDialog.dialogMode === 'mod') {
        return '세트 수정'
      }
    }
  }
}
</script>
