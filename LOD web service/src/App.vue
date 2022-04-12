<template>
  <div id="app">
    <el-container>
      <el-aside class="header">
        <Header />
      </el-aside>

    </el-container>
      
      <router-view class="el-main"/>
      
      <Footer class="footer"/>
  </div>
</template>

<script>
import Header from './components/layout/Header'
import Footer from './components/layout/Footer'

export default {
  name: 'app',
  components: {
    Header,
    Footer
  }
}
</script>

<style>
 * {
   box-sizing: border-box;
   margin: 0;
   padding: 0;
   font: inherit;
 }

 body {
   /* font-family: Arial, Helvetica, sans-serif; */
   line-height: 1.4;
 }

 .el-aside {
   position: relative;
   /* background: #545c64; */
   color: white;
   min-width:  100%;   
   min-height: 155px; 
   background: #3b4346;
    border-bottom: solid 1px #272d30;
    box-shadow: inset 0px -1px 0px 0px #51575a;
    text-shadow: -1px -1px 1px rgba(0, 0, 0, 0.75);
 }

 .logo-container {
   padding: 22px;
   background: #434A50;
 }

 .el-menu {
   border-right: 0 !important;
 }

 .el-main {
   background: #c5cfda;
   padding: 45px 0 45px 0;
 }

 .header a {
     color: #fff;
     padding-right: 5px;
     text-decoration: none;
 }

 .footer {
   background: #D4D9DD;
    text-shadow: 1px 1px 0px rgba(255, 255, 255, 0.5);

 }
</style>
