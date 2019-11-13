package main

import (
        "fmt"
        "os"
        "io"
        "net/http"
        "regexp"
        "strings"
        "time"
        "github.com/otiai10/gosseract"
)

// go get -u github.com/otiai10/gosseract
// git clone http://www.github.com/otiai10/gosseract
func main() {

        fmt.Println("工程公司图像识别服务开始...")

        // 处理器
        look := LookHandler{}

        //127.0.0.1:8081
        server := http.Server{
                Addr: "0.0.0.0:8081",
        }
        http.Handle("/look", &look)
        server.ListenAndServe()
        fmt.Println("end...")
}


type LookHandler struct{}

func (h *LookHandler) ServeHTTP(w http.ResponseWriter, r *http.Request) {
    r.ParseMultipartForm(1024000)
    //imgFile, imgHead, imgErr := r.FormFile("img")
    imgFile,_,imgErr := r.FormFile("img")
    if imgErr != nil {
         fmt.Println(imgErr)
         return
    }
    defer imgFile.Close()

    //imagename := "imgs/"+imgHead.Filename+".png"
    imagename := "imgs/myimage.png"
    image, err := os.Create(imagename)
    if err != nil {
        fmt.Println(err)
        return
    }
    defer image.Close()

    _, err = io.Copy(image, imgFile)
    if err != nil {
        fmt.Println(err)
        return
    } 

        text := Look(imagename)
        fmt.Println("开始时间：",time.Now())
        fmt.Println(text)
        fmt.Println("")
        os.Remove(imagename) 
        list := GetList(text)
        json := `{"images":[`
        name := GetName(list)
        json = json + `{"name":"项目名称","vlues":"` + name + `"},`
        a := GetA(list)
        json = json + `{"name":"甲方","vlues":"` + a + `"},`
        b := GetB(list)
        json = json + `{"name":"乙方","vlues":"` + b + `"},`
        number := GetNumber(list)
        json = json + `{"name":"合同编号","vlues":"` + number + `"}`
        json = json + `]}`
        fmt.Fprintf(w, json)
}




func GetList(str string) [100]string{
        str = strings.Replace(str, " ", "", -1) //空格去掉
        str = strings.Replace(str, "\n", "@", -1)
        str = strings.Replace(str, "\r", "@", -1)
        str = strings.Replace(str, "\t", "@", -1)
        s := strings.Split(str, "@")
        var list [100]string
        var index int = 0
        for _, rang := range s {
                if rang != "" {
                        list[index] = rang
                        index++
                }
        }
        return list
}

func GetNumber(list [100]string) string {

        var indexcurrent int = 0
        for index, scurrent := range list {
                if strings.Contains(scurrent, "合同编号:") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "合同编号：") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "合同编号=") {
                        indexcurrent = index
                        break
                }
        }
        reg := regexp.MustCompile(`合同编号:\s{0,2}(.{1,400})`)
        data := reg.FindStringSubmatch(list[indexcurrent])
        var sdata string
        if data != nil {
            sdata = data[1]
            if !IsEnd(list[indexcurrent+1]) {
                sdata = sdata + list[indexcurrent+1]
            }
            if !IsEnd(list[indexcurrent+2]) {
                sdata = sdata + list[indexcurrent+2]
            }
        }
        return sdata
}

func GetB(list [100]string) string {

        var indexcurrent int = 0
        for index, scurrent := range list {
                if strings.Contains(scurrent, "承包人:") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "承包人：") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "承包人=") {
                        indexcurrent = index
                        break
                }
        }
        reg := regexp.MustCompile(`承包人:\s{0,2}(.{1,400})`)
        data := reg.FindStringSubmatch(list[indexcurrent])
        var sdata string
        if data != nil {
            sdata = data[1]
            if !IsEnd(list[indexcurrent+1]) {
                sdata = sdata + list[indexcurrent+1]
            }
            if !IsEnd(list[indexcurrent+2]) {
                sdata = sdata + list[indexcurrent+2]
            }
        }
        return sdata
}

func GetA(list [100]string) string {

        var indexcurrent int = 0
        for index, scurrent := range list {
                if strings.Contains(scurrent, "发包人:") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "发包人：") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "发包人=") {
                        indexcurrent = index
                        break
                }
        }

        reg := regexp.MustCompile(`发包人:\s{0,2}(.{1,400})`)
        data := reg.FindStringSubmatch(list[indexcurrent])
        var sdata string
        if data != nil {
            sdata = data[1]
            if !IsEnd(list[indexcurrent+1]) {
                sdata = sdata + list[indexcurrent+1]
            }
            if !IsEnd(list[indexcurrent+2]) {
                sdata = sdata + list[indexcurrent+2]
            }
        }
        return sdata
}

func GetName(list [100]string) string {

        var indexcurrent int = 0
        for index, scurrent := range list {
                if strings.Contains(scurrent, "名称:") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "名称：") {
                        indexcurrent = index
                        break
                }
                if strings.Contains(scurrent, "名称=") {
                        indexcurrent = index
                        break
                }
        }

        reg := regexp.MustCompile(`名称:\s{0,2}(.{1,400})`)
        data := reg.FindStringSubmatch(list[indexcurrent])
        var sdata string
        if data != nil {
            sdata = data[1]
            if !IsEnd(list[indexcurrent+1]) {
                sdata = sdata + list[indexcurrent+1]
            }
            if !IsEnd(list[indexcurrent+2]) {
                sdata = sdata + list[indexcurrent+2]
            }
        }
        return sdata
}

func IsEnd(s string) bool {
        b := strings.ContainsAny(s, ":=")
        return b
}


//保存文件
func Write(text,filepath string) {
        f, err := os.OpenFile(filepath, os.O_RDWR|os.O_CREATE|os.O_TRUNC, 0777)
        if err != nil {
            fmt.Printf("open err%s", err)
            return
        }
        defer f.Close() //资源必须释放,函数刚要返回之前延迟执行
        _, _ = f.WriteString(text)
}



//分析图像的文字
func Look(imagepath string) string {
    client := gosseract.NewClient()
    defer client.Close()

    client.SetLanguage("chi_sim")
    client.SetImage(imagepath)
    text, _ := client.Text()
    return text
}