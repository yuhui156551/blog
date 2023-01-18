<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      馀灰博客管理系统
    </div>
    <div>
      看不清未来，就把握好现在
    </div>
    <div>
      馀灰GitHub地址：https://github.com/yuhui156551
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'Dashboard',
  computed: {
    ...mapGetters([
      'name'
    ])
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }
  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}
</style>
