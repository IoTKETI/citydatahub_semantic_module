<template>
    <div>
        <h1 >Data Hub LOD</h1>
        <el-menu mode="horizontal" default-active="2" class="nav"
          background-color="#3b4346" text-color="#fff" active-text-color="#ffd04b">

          <el-menu-item index="3">
            <!-- <i class="el-icon-menu"></i> -->
            <router-link to="/home" style="font-size:1.5em">Home</router-link>
          </el-menu-item>

          <el-menu-item aria-hidden="true"/>

          <el-menu-item index="4">
            <!-- <i class="el-icon-menu"></i> -->
            <router-link to="/search" style="font-size:1.5em">Search</router-link>
          </el-menu-item>

          <el-menu-item aria-hidden="true"/>

          <el-menu-item index="5">
            <!-- <i class="el-icon-menu"></i> -->
            <router-link to="/select" style="font-size:1.5em">Select</router-link>
          </el-menu-item>

          <el-menu-item aria-hidden="true"/>

          <el-menu-item index="6">
            <!-- <i class="el-icon-menu"></i> -->
            <router-link to="/detail" style="font-size:1.5em">Detail</router-link>
          </el-menu-item>

          <a/>

        </el-menu>
    </div>

</template>

<script>
export default {
    name: "Header"
}
</script>

<style scoped>
.el-menu.el-menu--horizontal {
    border-bottom: none;

}

h1 {
    position: absolute;
    bottom: 35px;
    left: 3%;
    font-size: 3em;
}

.nav {
    position: absolute;
    right: 0;
    bottom: 35px;
    font-size: 2.25em;
    margin-right: 20px;
}

</style>
