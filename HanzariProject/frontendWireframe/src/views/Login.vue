<template>
  <v-app id="app">
    <v-app-bar app dark>
      <v-toolbar color="black" dark>
        <v-spacer />
        <v-toolbar-title>Hanzari Login</v-toolbar-title>
      </v-toolbar>
    </v-app-bar>
    <v-main>
      <v-container style="position: relative; top: 20%" class="text-xs-center">
        <v-layout row wrap class="text-xs-center">
          <v-flex>
            <v-card flat class="mx-auto" max-width="800">
              <v-card-title primary-title>
                <h4>Login</h4>
              </v-card-title>
              <v-form>
                <v-text-field
                  prepend-icon="person"
                  name="Employee ID"
                  label="Employee ID"
                ></v-text-field>
                <v-text-field
                  prepend-icon="lock"
                  name="Password"
                  label="Password"
                  type="password"
                ></v-text-field>
                <v-card-actions>
                  <v-btn primary large block>Login</v-btn>
                </v-card-actions>
              </v-form>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-main>
  </v-app>
</template>

<script>
export default {};
</script>