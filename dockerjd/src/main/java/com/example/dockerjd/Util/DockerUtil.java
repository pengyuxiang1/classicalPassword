package com.example.dockerjd.Util;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.*;
import com.github.dockerjava.api.model.*;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.command.EventsResultCallback;

import java.io.*;
import java.util.List;

import static com.github.dockerjava.api.model.HostConfig.newHostConfig;

/**
 * @Author: permission
 * @Description:
 * @Date: Create in 21:24 2019/10/23
 * @Modified By:
 */
public class DockerUtil {
    private DockerClient dockerClient;
    /**
     * 连接docker服务器
     * @return
     */
    public DockerClient connectDocker(){
//        DockerClient dockerClient = DockerClientBuilder.getInstance("tcp://101.132.120.137:2375").build();
        DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().withDockerTlsVerify(true)
                .withDockerHost("tcp://101.132.120.137:2375").withDockerCertPath("E:/cert/").withDockerConfig("E:/cert/").build();
        dockerClient = DockerClientBuilder.getInstance(config).build();
        Info info = dockerClient.infoCmd().exec();
        String infoStr = JSONObject.toJSONString(info);
        System.out.println("docker的环境信息如下：=================");
        System.out.println(infoStr);
        return dockerClient;
    }

    /**
     * 创建容器
     * @param
     * @return
     */
    public CreateContainerResponse createContainers(String containerName, String imageName){
        //映射端口8088—>80
        ExposedPort tcp80 = ExposedPort.tcp(80);
        Ports portBindings = new Ports();
        portBindings.bind(tcp80, Ports.Binding.bindPort(8088));

        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withName(containerName)
                .withHostConfig(newHostConfig().withPortBindings(portBindings))
                .withExposedPorts(tcp80).exec();
        return container;
    }

    /**
     * 启动容器
     * @param
     * @param containerId
     */
    public void startContainer(String containerId){
//        StartContainerCmd startContainerCmd=dockerClient.startContainerCmd(containerId).exec();
        StartContainerCmd startContainerCmd=dockerClient.startContainerCmd(containerId);
        startContainerCmd.exec();

    }

    /**
     * 停止容器
     * @param
     * @param containerId
     */
    public void stopContainer(String containerId){
        dockerClient.stopContainerCmd(containerId).exec();
    }

    /**
     * 删除容器
     * @param
     * @param containerId
     */
    public void removeContainer(String containerId){
        dockerClient.removeContainerCmd(containerId).exec();
    }
    /**
     *  @Author: permission
     *  @Description: 连接到容器内部
     *  @Date: 2019/10/23 21:58
     *  @Param []
     *  @Return: void
     */
    public void connectC(String containerId) throws IOException {

    }

    /**
     *  @Author: permission
     *  @Description: 获得容器内的输入输出
     *  @Date: 2019/10/28 12:32
     *  @Param []
     *  @Return: void
     */
    public void getInfoFromC(){

    }

    /**
     *  @Author: permission
     *  @Description:
     *  @Date: 2019/10/24 20:29
     *  @Param [imgName]
     *  @Return: void
     */
    public void searchImage(String imgName){

        List<SearchItem> dockerSearch = dockerClient.searchImagesCmd(imgName).exec();
        System.out.println("Search returned" + dockerSearch.toString());
    }

    public void eventsListen() throws InterruptedException, IOException {
        EventsResultCallback callback = new EventsResultCallback() {
            public void onNext(Event event) {
                System.out.println("Event: " + event);
                super.onNext(event);
            }
        };
        dockerClient.eventsCmd().exec(callback).awaitCompletion().close();

    }

    /**
     *  @Author: permission
     *  @Description: 创建容器，获得执行id（好像就是容器id本身）
     *  @Date: 2019/10/28 12:33
     *  @Param
     *  @Return:
    */
    private void execCreate() throws IOException {
        ExecCreateCmd execCreateid=dockerClient.execCreateCmd("f2832f9aa393c9c033eca38a4f3554c542bbcdb4444cfced7c45211c6dcc5d5a");
        System.out.println(execCreateid.toString());
        ExecStartCmd execStartCmd=dockerClient.execStartCmd("f2832f9aa393c9c033eca38a4f3554c542bbcdb4444cfced7c45211c6dcc5d5a");
        System.out.println(execStartCmd.getExecId());
//        InspectExecCmd inspectExecResponse=dockerClient.inspectExecCmd(execStartCmd.getExecId());
//        System.out.println(inspectExecResponse.exec().toString());
//        System.out.println(execStartCmd.getExecId());
//        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(execStartCmd.getStdin()));
//        System.out.println(bufferedReader.readLine());
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        DockerUtil dockerClientService =new DockerUtil();
        //连接docker服务器
        DockerClient client = dockerClientService.connectDocker();
        System.out.println("--------------------------------------------------------------------------------");
        dockerClientService.execCreate();
        System.out.println("--------------------------------------------------------------------------------");
        //创建容器
//        CreateContainerResponse container = dockerClientService.createContainers("nginx1","nginx:latest");
//        //启动容器
//        dockerClientService.startContainer(container.getId());
    }

}
