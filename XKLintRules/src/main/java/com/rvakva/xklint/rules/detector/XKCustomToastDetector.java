package com.rvakva.xklint.rules.detector;

import com.android.tools.lint.client.api.JavaParser;
import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.LintFix;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.SourceCodeScanner;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.impl.source.PsiMethodImpl;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.UMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;

/**
 * @Copyright (C), 2012-2019, Sichuan Xiaoka Technology Co., Ltd.
 * @FileName: XKCustomToastDetector
 * @Author: hufeng
 * @Date: 2019/9/25 下午2:08
 * @Description:
 * @History:
 */
public class XKCustomToastDetector extends Detector implements SourceCodeScanner {


    private static final Class<? extends Detector> DETECTOR_CLASS = XKCustomToastDetector.class;
    private static final EnumSet<Scope> DETECTOR_SCOPE = Scope.JAVA_FILE_SCOPE;
    private static final Implementation IMPLEMENTATION = new Implementation(
            DETECTOR_CLASS,
            DETECTOR_SCOPE
    );

    private static final String ISSUE_ID = "XK_ToastUseError";
    private static final String ISSUE_DESCRIPTION = "警告:你应该使用我们团队自定义的Toast工具类{ToastUtil}";
    private static final String ISSUE_EXPLANATION = "你不能直接使用Toast，你应该使用我们团队自定义的Toast工具类{ToastUtil}";
    private static final Category ISSUE_CATEGORY = Category.CORRECTNESS;
    private static final int ISSUE_PRIORITY = 9;
    private static final Severity ISSUE_SEVERITY = Severity.ERROR;
    private static final String CHECK_CODE = "Toast";
    private static final String CHECK_PACKAGE = "android.widget.Toast";

    public static final Issue ISSUE = Issue.create(
            ISSUE_ID,
            ISSUE_DESCRIPTION,
            ISSUE_EXPLANATION,
            ISSUE_CATEGORY,
            ISSUE_PRIORITY,
            ISSUE_SEVERITY,
            IMPLEMENTATION
    );

    @Override
    public List<String> getApplicableMethodNames() {
        return Collections.singletonList("makeText");
    }

    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        if (!context.getEvaluator().isMemberInClass(method, CHECK_PACKAGE)) {
            return;
        }

        List<UExpression> args = node.getValueArguments();
        UExpression duration = null;
        if (args.size() == 3) {
            duration = args.get(2);
        }
        LintFix fix = null;
        if (duration != null) {
            String replace;
            if ("Toast.LENGTH_LONG".equals(duration.toString())) {
                replace = "ToastUtils.showLong(" + args.get(0).toString() + ", " + args.get(1).toString() + ");";
            } else {
                replace = "ToastUtils.showShort(" + args.get(0).toString() + ", " + args.get(1).toString() + ");";
            }
            fix = fix().name("Replace with ToastUtils")
                    .replace()
                    .with(replace)
                    .build();
        }

        context.report(ISSUE, node, context.getLocation(node), ISSUE_DESCRIPTION, fix);

    }

}
