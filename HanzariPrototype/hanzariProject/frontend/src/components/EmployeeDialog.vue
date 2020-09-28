<template>
  <v-dialog max-width="290" :value="dialog">
    <v-form ref="form" lazy-validation>
      <v-card>
        <v-card-title>{{dialogStatus}}</v-card-title>
        <v-card-text>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn @click="$emit('close')">Close</v-btn>
        </v-card-actions>
      </v-card>
    </v-form>
  </v-dialog>
</template>

<script>
export default {
  props: {
    dialogStatus: {
        type: Boolean
    }
  },
  data () {
    return {
    }
  },
    directives: {
		'click-outside': {
			bind: function (el, binding, vnode) {
				el.event = function (event) {
					if (!(el == event.target || el.contains(event.target))) {
						vnode.context[binding.expression](event);
					}
				};
				var elementsOfOutside = document.querySelectorAll('.clickOutsideOfLectureCard');
				for (var i = 0; i < elementsOfOutside.length; i++) {
					elementsOfOutside[i].addEventListener('click', el.event);
				}
			},
			unbind: function (el) {
				var elementsOfOutside = document.querySelectorAll('.clickOutsideOfLectureCard');
				for (var i = 0; i < elementsOfOutside.length; i++) {
					elementsOfOutside[i].removeEventListener('click', el.event);
				}
			}
		}
  },
  computed: {
    dialog () {
      return this.dialogStatus
    }
  }
}
</script>
