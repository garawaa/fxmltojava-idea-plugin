package io.github.garawaa.fxml2java.plugin;

import com.intellij.ide.projectView.ProjectView;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectFileIndex;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.krm.fxml2java.FXMLToJavaConvertor;
import com.krm.fxml2java.MainClass;
import org.jetbrains.annotations.NotNull;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MenuHandleAction extends AnAction {
    FXMLToJavaConvertor convertor;
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String path = vFile.getPath();
        String fileName = vFile != null ? vFile.getName() : null;
        String[] fn = fileName.split("\\.");
        String ext = "";
        if (fn.length>1) {
            ext = fn[1];
        } else {
            return;
        }
        String sourcePath = ProjectFileIndex.SERVICE.getInstance(e.getProject()).getSourceRootForFile(vFile.getParent()).getPath();
        String relativePath = VfsUtilCore.getRelativePath(vFile.getParent(), ProjectFileIndex.SERVICE.getInstance(e.getProject()).getSourceRootForFile(vFile.getParent()));
        String packageName = ProjectFileIndex.SERVICE.getInstance(e.getProject()).getPackageNameByDirectory(vFile.getParent());
        String className = fn[0]+"Base";
        className = className.substring(0,1).toUpperCase()+className.substring(1);
        try {
            convertor = new FXMLToJavaConvertor();
        } catch (ParserConfigurationException parserConfigurationException) {
            parserConfigurationException.printStackTrace();
        }

        String newClassName = Messages.showInputDialog("Class name: ", "Generate code", null, className, null);
        if (newClassName!=null && !newClassName.equals(""))
            className = newClassName;
        else
            return;

        try {
            MainClass mainClass = new MainClass(className, packageName);
            mainClass.setClassModifiers(new int[] { 1, 1024 });
            mainClass.setNodeModifiers(new int[] { 4, 16 });
            mainClass.setMethodModifiers(new int[] { 4, 1024 });
            convertor.convert(mainClass, path);
            String outputFileName = sourcePath+"/"+relativePath+"/"+className+".java";
            String content = mainClass.toString();
            File file = new File(outputFileName);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
            fw.close();
            ProjectView.getInstance(e.getProject()).refresh();
            VirtualFileManager.getInstance().syncRefresh();

            Messages.showMessageDialog("File created at path:\n"+outputFileName, "Successful", Messages.getInformationIcon());
        } catch (IOException ioException) {
            Messages.showErrorDialog(ioException.getMessage(), "Failed");
        }

    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        super.update(e);
        //Doesn't show menu if project is not open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
        //Doesn't show menu if file extension is not fxml
        VirtualFile vFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        String fileName = vFile != null ? vFile.getName() : null;
        if (fileName==null)
            return;
        String[] fn = fileName.split("\\.");
        String ext = "";
        if (fn.length>1) {
            ext = fn[1];
        }
        if (ext.equals("") || !ext.equals("fxml")){
            e.getPresentation().setEnabledAndVisible(false);
        }
    }
}

