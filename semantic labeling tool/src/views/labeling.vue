<template>
  <div>
    <b-container class="bv-example-row">
    <b-card shadow="always" style="margin-top: 10px; margin-bottom:30px;">
      <!-- <H1></H1> -->
      <br>
      <b-row >
        <b-col class="mb-1" md="2" offset-md="">
          <b-form-textarea
            style="background:#b5bec3;"
            id="textarea-no-resize"
            placeholder="Enter ID"
            v-model="set_id"
            rows="1"
            no-resize
          ></b-form-textarea>
        </b-col>
        <b-col md ="2">
            <b-button variant="success" @click="get_requst">Get Rule Set</b-button>
        </b-col>
        <b-col md ="6" offset-lg="2">
            <b-form-file
              v-model="csvfile"
              :state="Boolean(csvfile)"
              placeholder="Choose a file or drop it here..."
              drop-placeholder="Drop file here..."
            ></b-form-file>
        </b-col>
      </b-row>
      <b-row class="mb-1">
        <b-col md = "" offset-md = "">
          <b-form-textarea
            style="background:#b5bec3;"
            id="textarea"
            v-model="full_text_box"
            placeholder="sourceValue(?x,'m')->targetValue(?x,'meter')"
            rows="6"
            no-resize
          ></b-form-textarea>
        </b-col>
      </b-row>
      <b-row class="justify-content-md-center" >
        <b-col class="mb-1" md="2" offset-md="">
          <b-button variant="outline-info" @click="rulcreate_requst">Create Rule</b-button>
        </b-col>
      </b-row>
      <b-row class="justify-content-md-end" >
        <b-col class="mb-1" md="2" offset-lg="">
          <b-button variant="outline-success" @click="ruleexecution_request">Generate Labels</b-button>
        </b-col>
      </b-row>
      <b-row>
        <b-col class="" md="3" offset-lg="">
          <b-button size = "lg" class="m-1" variant="outline-warning" @click="btn_add_text(btnitem[0])">-></b-button>
          <b-button size = "lg" class="m-1" variant="outline-primary" @click="btn_add_text(btnitem[1])">^</b-button>
        </b-col>
        <b-col class="" md="3" offset-lg="">
          <b-button size = "lg" class="m-1" variant="outline-primary" @click="btn_add_text(btnitem[2])">=</b-button>
          <b-button size = "lg" class="m-1" variant="outline-primary" @click="btn_add_text(btnitem[3])">≠</b-button>
          <b-button size = "lg" class="m-1" variant="outline-primary" @click="btn_add_text(btnitem[4])">[ ]</b-button>
        </b-col>
        <b-col class="mb-1" md="2" offset-lg="2">
          <b-button size = "lg"  class="m-1" variant="outline-danger" @click="delete_text">Delete</b-button>
        </b-col>
      </b-row>
      <b-row>
        <b-col class="md-2" md="2 " offset-lg="">
           <b-form-select class="m-3" style="width:105%" v-model="var_selected" :options="var_options" @change="selet_add_text"></b-form-select>

        </b-col>
        <b-col class="md-2" md="2 " offset-lg="">
          <b-form-select  class="m-3" style="width:105%" v-model="swr_selected" :options="swr_options"  @change="selet_add_text"></b-form-select>        
        </b-col>
        <b-col class="md-2" md="3 " offset-lg="">
          <b-form-select class="m-3" style="width:105%" v-model="xml_selected" :options="xml_options"  @change="selet_add_text"></b-form-select>        
        </b-col>
        <b-col class="md-2" md="2 " offset-lg="">
          <b-form-select class="m-3" style="width:105%" v-model="pro_selected" :options="pro_options"  @change="selet_add_text"></b-form-select>        
        </b-col>
        <b-col class="md-2" md="2 " offset-lg="">
          <b-form-select class="m-3" style="width:105%" v-model="rule_selected" :options="rule_options" @change="selet_add_text"></b-form-select>        
        </b-col>
      </b-row>
     </b-card>
    </b-container>
     <b-container>
       <b-card shadow="always" style="margin-bottom:30px;">
         <h4>Add Parameters</h4>
         <b-row>
           <b-col>
            <b-form-group  v-slot="{ ariaDescribedby }">
              <b-form-radio-group
                v-model="add_selected"
                :options="add_options"
                :aria-describedby="ariaDescribedby"
                name="plain-inline"
                plain
              ></b-form-radio-group>
            </b-form-group>
           </b-col>
          <b-col>
          <b-form-textarea
            size = "sm"
            style="background:#b5bec3;"
            id="textarea-no-resize"
            placeholder="Enter ID"
            v-model = "var_add"
            no-resize
          ></b-form-textarea>
           </b-col>
           <b-col>
            <b-button class ="m-1" variant="outline-info" size = "" @click="selete_add_item">Add Value</b-button>
            <b-button variant="outline-danger" size = "" @click="selete_delete_item">Delete Value</b-button>
          </b-col>
         </b-row>
     </b-card>
     
    </b-container>
  </div>
</template>

<script>
import axios from "axios"
  export default {
    data() {
      return {
        res_errmess:"",
        csvfile: null,
        btnitem:[1,2,3,4,5],
        set_id : "",
        full_text_box: "",
        var_selected: null,
        swr_selected: null,
        xml_selected: null,
        pro_selected: null,
        rule_selected: null,
        add_selected:"Variable",
        var_add:"",
        err_message: "",
        mes_status:"",
        var_options: [
          { value: null, text: 'variable'},
          // { value: '?x', text: '?x' },
          // { value: '?y', text: '?y'},
        ],
        swr_options: [
          { value: null, text: 'SWRL Builtin' },
          { value: 'swrl:append', text: 'swrl:append' },
          { value: 'swrl:avg', text: 'swrl:avg'},
          { value: 'swrl:count', text: 'swrl:count'}
        ],
        xml_options: [
          { value: null, text: 'XML schema datatype' },
          { value: 'xsd:anyURL', text: 'xsd:anyURL' },
          { value: 'xsd:dateTime', text: 'xsd:dateTime'},
          { value: 'xsd:double', text: 'xsd:double' },
        ],
        pro_options: [
          { value: null, text: 'property' },
          // { value: 'sourceValue', text: 'sourceValue' },
          // { value: 'targetValue', text: 'targetValue'},
        ],
        rule_options: [
          { value: null, text: 'SWRL Rule' },
          { value: 'rule_1', text: 'rule_1' },
          { value: 'rule_2', text: 'rule_2'},
        ],
        add_options: [
          { value: "variable", text: 'Variable'},
          { value: 'swrl', text: 'SWRL Builtin' },
          { value: 'xml', text: 'XML schema datatype'},
          { value: 'property', text: 'Property'},
          { value: 'rule', text: 'SWRL Rule'},
        ],
      }
    },
    methods:{
      selet_add_text(evt){
          let in_val = evt;
          let out_val = '';
          // console.log(val);
          if(in_val !==null){
            out_val = in_val
          }
            return this.full_text_box = this.full_text_box + out_val
      },
      btn_add_text(item){ 
        let out_val = '';
        if(item === 1 ){
          out_val = '->';
        }
        else if(item === 2){
          out_val = '^';
        }
        else if(item === 3){
          out_val = '=';
        }
        else if(item === 4){
          out_val = '≠';
        }
        else if(item === 5){
          out_val = '[]';
        }
        return this.full_text_box = this.full_text_box + out_val;
      },
      delete_text(){ 

        return this.full_text_box = "";
      },
      selete_add_item(){
        if(this.add_selected == "variable"){
          if(this.var_add != ""){
            this.var_options.push({ value: this.var_add, text: this.var_add})
          }
          else{
            alert("값을 입력해주세요")
          }
        }else if(this.add_selected == "swrl"){
          if(this.var_add != ""){
            this.swr_options.push({ value: this.var_add, text: this.var_add})
          }
          else{
            alert("값을 입력해주세요")
          }
        }else if(this.add_selected == "xml"){
          if(this.var_add != ""){
            this.xml_options.push({ value: this.var_add, text: this.var_add})
          }
          else{
            alert("값을 입력해주세요")
          }
        }else if(this.add_selected == "property"){
          if(this.var_add != ""){
            this.pro_options.push({ value: this.var_add, text: this.var_add})
          }
          else{
            alert("값을 입력해주세요")
          }
        }else if(this.add_selected == "rule"){
          if(this.var_add != ""){
            this.rule_options.push({ value: this.var_add, text: this.var_add})
          }
          else{
            alert("값을 입력해주세요")
          }
        }else{
          alert("Check box를 선택해주세요")
        }
      },
       selete_delete_item(){
          let delete_item = this.var_add;
          if(this.add_selected == "variable"){
                if(delete_item != ""){
                  const itemToFind = this.var_options.find(function(item)
                  {return item.value === delete_item})
                  const idx = this.var_options.indexOf(itemToFind)
                  if (idx > -1) this.var_options.splice(idx, 1)
                
                }
                else{
                  alert("값을 입력해주세요")
                }
          }else if(this.add_selected == "swrl"){
                if(delete_item != ""){
                  const itemToFind = this.swr_options.find(function(item)
                  {return item.value === delete_item})
                  const idx = this.swr_options.indexOf(itemToFind)
                  if (idx > -1) this.swr_options.splice(idx, 1)
                }
                else{
                  alert("값을 입력해주세요")
                }
          }else if(this.add_selected == "xml"){
                if(delete_item != ""){
                    const itemToFind = this.xml_options.find(function(item)
                    {return item.value === delete_item})
                    const idx = this.xml_options.indexOf(itemToFind)
                    if (idx > -1) this.xml_options.splice(idx, 1)
                }
                else{
                    alert("값을 입력해주세요")
                }
          }else if(this.add_selected == "property"){
                if(delete_item != ""){
                    const itemToFind = this.pro_options.find(function(item)
                    {return item.value === delete_item})
                    const idx = this.pro_options.indexOf(itemToFind)
                    if (idx > -1) this.pro_options.splice(idx, 1)
                }
                else{
                    alert("값을 입력해주세요")
                }
          }else if(this.add_selected == "rule"){
                if(delete_item != ""){
                    const itemToFind = this.rule_options.find(function(item)
                    {return item.value === delete_item})
                    const idx = this.rule_options.indexOf(itemToFind)
                    if (idx > -1) this.rule_options.splice(idx, 1)
                }
                else{
                    alert("값을 입력해주세요")
                }
          }else{
                alert("Check box를 선택해주세요")
          }
      },
      rulcreate_requst(){
        console.log(this.full_text_box);
        let url = "http://192.168.50.154:9090/api/axiom-generator/v1/rules"
        let body = {};
        body.id = this.set_id
        body.dataProperties = []
        body.variables = []
        this.pro_options.forEach(element => {
              if(element.value != null){
                body.dataProperties.push(element.text); 
              }
        });
        const regExp = /[`~!@#$%^&*()_|+\-=?;:'",.<>]/gi
        this.var_options.forEach(element => {
              if(element.value != null){
                let split_variable = element.value.replace(regExp,"")
                body.variables.push(split_variable); 
              }
        });
        body.rule = this.full_text_box;
        axios.post(url, body)
          .then(response =>{
            console.log(response.status)

          })
          .catch(error => {
            console.log(error);
          });
      },
      get_requst(){
        let url = 'http://192.168.50.154:9090/api/axiom-generator/v1/rules?id='+ this.set_id
        //http://192.168.50.154:9090/api/axiom-generator/v1/swrl?id=swrl_set_1
        axios.get(url)
          .then(response =>{
              this.var_options = [];
              this.pro_options = [];
              this.var_options.push({ value:null, text: "variable"})
              this.pro_options.push({ value:null, text: "property"})
              let varials_arr = response.data.variables
              varials_arr.forEach(element => {
                this.var_options.push({ value: "?"+element, text: "?"+element})
              });
              let property_arr = response.data.dataProperties
              property_arr.forEach(element => {
                this.pro_options.push({ value: element, text: element})
              });
              let text_rule = response.data.rule
              return this.full_text_box = text_rule
          })
          .catch(error => {
              this.err_message = error
              if(error.response.status === 404){
                this.mes_status = error.response.status
              }
              console.log(this.err_message, this.mes_status);
          });
      },
      ruleexecution_request(){
        // console.log(this.csvfile);
        if(this.set_id != ""){
            let url = "http://192.168.50.154:9090/api/axiom-generator/v1/execute?ruleId="+this.set_id
            // // http://192.168.50.154:9090/api/axiom-generator/v1/execute?ruleId=swrl_set_1
            let formData = new FormData();
            formData.append("file", this.csvfile);
            axios.post(url, formData)
              .then(response =>{
                    let text = response.data;
                    var downloadLink = document.createElement("a");
                    var blob = new Blob([text], { type: "text/csv;charset=utf-8" });
                    var url = URL.createObjectURL(blob);
                    downloadLink.href = url;
                    downloadLink.download = this.set_id+".csv";

                    document.body.appendChild(downloadLink);
                    downloadLink.click();
                    document.body.removeChild(downloadLink);
                    // formData.append("file", response.Data);
                    // console.log(formData);
              })
              .catch(error => {
                  console.log(error);
                  this.res_errmess = error.response.data
                  if(error.response.status === 400){
                    // this.res_status = error.response.status
                    alert("Please enter CSV.File")
                  }else if(error.response.status === 404){
                    this.res_status = error.response.status  
                  }
              });
          }else{
            alert("Please enter your ID")
          }
      }

    }
  }
</script>