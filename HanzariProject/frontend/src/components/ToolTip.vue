<template>
  <div class="text-center d-flex align-center justify-space-around">
    <v-tooltip left>
      <template v-slot:activator="{ on, attrs }">
        <v-btn
          color="primary"
          dark
          v-bind="attrs"
          v-on="on"
        >
          Left
        </v-btn>
      </template>
      <span>Left tooltip</span>
    </v-tooltip>
  </div>
</template>