package com.rvakva.xklint.rules.detector;

import com.android.annotations.Nullable;
import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.intellij.psi.PsiMethod;
import com.sun.istack.NotNull;

import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UReferenceExpression;
import org.jetbrains.uast.UastUtils;
import org.jetbrains.uast.util.UastExpressionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: ThreadDetector
 * @Author: hufeng
 * @Date: 2019/9/26 上午11:46
 * @Description:
 * @History:
 */
public class ThreadDetector extends Detector implements Detector.UastScanner {

    private final String NEW_THREAD = "java.lang.Thread";
    public static final Issue ISSUE = Issue.create(
            "XK_ThreadUsage",
            "避免自己创建Thread",
            "请勿直接调用new Thread()，建议使用统一的线程管理工具类",
            Category.CORRECTNESS,
            6,
            Severity.WARNING,
            new Implementation(ThreadDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Override
    public List<String> getApplicableConstructorTypes() {
        return Collections.singletonList("java.lang.Thread");
    }

    @Override
    public void visitConstructor(@NotNull JavaContext context,
                                 @NotNull UCallExpression node,
                                 @NotNull PsiMethod constructor) {
        context.report(ISSUE, node, context.getLocation(node),
                "避免自己创建Thread");
    }
}