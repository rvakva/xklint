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
import org.jetbrains.uast.util.UastExpressionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: LogDetector
 * @Author: hufeng
 * @Date: 2019/9/26 上午11:45
 * @Description:
 * @History:
 */
public class LogDetector extends Detector implements Detector.UastScanner {

    public static final Issue ISSUE = Issue.create(
            "XK_LogUsage",
            "请不要直接使用Log的方式打印日志，而应该使用公共组件中的LogUtil",
            "使用公共组件便于统一管理日志的打印和不打印！",
            Category.CORRECTNESS,
            6,
            Severity.ERROR,
            new Implementation(LogDetector.class, Scope.JAVA_FILE_SCOPE)
    );

    @Nullable
    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        return Collections.singletonList(UCallExpression.class);
    }

    @Nullable
    @Override
    public UElementHandler createUastHandler(@NotNull JavaContext context) {
        return new LogHandler(context);
    }

    class LogHandler extends UElementHandler {

        private JavaContext context;

        LogHandler(JavaContext context) {
            this.context = context;
        }

        @Override
        public void visitCallExpression(@NotNull UCallExpression node) {
            if (!UastExpressionUtils.isMethodCall(node)) return;
            if (node.getReceiver() != null
                    && node.getMethodName() != null) {
                String methodName = node.getMethodName();
                if (methodName.equals("i")
                        || methodName.equals("d")
                        || methodName.equals("e")
                        || methodName.equals("v")
                        || methodName.equals("w")
                        || methodName.equals("wtf")) {
                    PsiMethod method = node.resolve();
                    if (context.getEvaluator().isMemberInClass(method, "android.util.Log")) {
                        reportAllocation(context, node);
                    }
                }
            }
        }
    }

    private void reportAllocation(JavaContext context, UCallExpression node) {
        context.report(ISSUE,
                node,
                context.getLocation(node),
                "\u21E2 请使用LogUtil进行日志输出！"
        );
    }
}