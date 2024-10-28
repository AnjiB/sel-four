package com.anji.sel.util;

import com.anji.sel.pojo.metric.MetricDto;
import java.util.List;

public class FilterUtil {

  public static List<MetricDto> filter(List<MetricDto> rawList) {
    return rawList.stream().filter(item -> !filterItem(item.getName())).toList();
  }

  private static boolean filterItem(String item) {
    List<String> filterList =
        List.of(
            "Timestamp",
            "AudioHandlers",
            "AudioWorkletProcessors",
            "Documents",
            "Frames",
            "JSEventListeners",
            "LayoutObjects",
            "MediaKeySessions",
            "MediaKeys",
            "Nodes",
            "Resources",
            "ContextLifecycleStateObservers",
            "V8PerContextDatas",
            "WorkerGlobalScopes",
            "UACSSResources",
            "RTCPeerConnections",
            "ResourceFetchers",
            "AdSubframes",
            "DetachedScriptStates",
            "ArrayBufferContents",
            "LayoutCount",
            "RecalcStyleCount",
            "LayoutDuration",
            "RecalcStyleDuration",
            "DevToolsCommandDuration",
            "V8CompileDuration",
            "TaskDuration",
            "TaskOtherDuration",
            "ThreadTime",
            "ProcessTime",
            "FirstMeaningfulPaint",
            "NavigationStart");
    return filterList.contains(item);
  }
}
