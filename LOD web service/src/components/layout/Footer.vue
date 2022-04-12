<template>
    <div class="footer" >
        <div class="cont"  style=" margin-left: 60px; margin-right: 100px;">
            <h2>Data Hub Project 참여기업 </h2>
            <div>
                <el-row :gutter="20" style="margin-right: 70px;">
                    <el-col :span="6">
                        <div class="company">KETI</div>
                        <div class="company">Daumsoft</div>
                        <div class="company">TTA</div>
                        <div class="company">N2M</div>
                    </el-col>
                    <el-col :span="6">
                        <div class="company">Sejong Univ.</div>
                        <div class="company">Anyang Univ.</div>
                        <div class="company">Innogrid</div>
                        <div class="company">나무기술</div>
                    </el-col>
                    <el-col :span="6">
                        <div class="company">Pusan Univ.</div>
                        <div class="company">Bitua Systems</div>
                        <div class="company">Dtonic</div>
                        <div class="company">N2M</div>
                    </el-col>
                    <el-col :span="6">
                        <div class="company">KAIST</div>
                        <div class="company">LGU+</div>
                        <div class="company">KAIST</div>
                        <div class="company">JC-square</div>
                    </el-col>
                </el-row>
            </div>
        </div>
        
        <div class="footer" >
            <div style="align:center; text-align:center">
                <img src="./../../assets/keti.png"/>
            </div>
        </div>
    </div>
    
</template>

<script>
export default {
    name: "Footer"
}
</script>

<style scoped>
.footer {
    padding: 45px 0 45px 0;
    text-shadow: 1px 1px 0px rgba(255, 255, 255, 0.5);
    margin: 0 auto;
    border-bottom: solid 1px #b5bec3;
    
}

.cont {
    width: calc(100% - 40px);
    margin: 0 auto;
    /* border-bottom: solid 0.5px; */
}

.company {
    border-bottom: solid 1px #b5bec3;
    padding: 0.2em 0 0.2em 0;
}

h2 {
    font-size: 1.25em;
    color: #212f35;
    margin: 0 0 1em 0;
}

.col-3 {
    width: 25%;
}

  .el-row {
    margin-bottom: 20px;

  }
  .el-col {
    border-radius: 4px;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }

</style>
