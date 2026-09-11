package com.twilio.oai.java.feature;

import com.twilio.oai.common.EnumConstants.SupportedOperation;
import org.openapitools.codegen.CodegenOperation;
import org.openapitools.codegen.CodegenParameter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The base `Reader` class in twilio-java already owns the page size: `pageSize(int)` and `limit(long)`
 * store it and `getPageSize()` is what `addQueryParams` puts on the wire. When the spec of a list
 * operation also declares a page-size query parameter, the generated reader kept a second copy of it and
 * serialized that one as well, so a reader could send the page size twice (DII-2465).
 *
 * This feature makes the parent the only owner of the value. The spec's page-size query parameter is
 * taken out of `queryParams`, which stops the per-parameter serialization from being generated for it,
 * and its name is handed to the templates for the single `getPageSize()` query parameter they emit. That
 * name is used verbatim: classic APIs spell it `PageSize` and API v1 spells it `pageSize`; the two are
 * distinct query parameters on the wire, so the spelling from the spec has to survive.
 *
 * The setter generated for the removed parameter is kept - it now writes through to the parent.
 */
public class PageSizeParameter {

    /** The page-size query parameter removed from `queryParams`, for the write-through setter. */
    public static final String PAGE_SIZE_PARAM = "x-page-size-param";
    /** Name to serialize `getPageSize()` under. */
    public static final String PAGE_SIZE_QUERY_NAME = "x-page-size-query-name";
    /** Whether the reader should call `addQueryParams`, i.e. whether the spec declared any query parameter. */
    public static final String ADD_QUERY_PARAMS = "x-add-query-params";

    /**
     * Readers of operations whose spec declares no page-size parameter keep sending `PageSize`: several
     * classic list endpoints accept it without declaring it.
     */
    private static final String DEFAULT_PAGE_SIZE_QUERY_NAME = "PageSize";
    private static final String PAGE_SIZE_BASE_NAME = "pagesize";
    /** The write-through setter calls `intValue()`, so only numeric page sizes can be delegated. */
    private static final List<String> DELEGATABLE_DATA_TYPES = List.of("Integer", "Long");

    public static PageSizeParameter instance;

    private PageSizeParameter() { }

    public static synchronized PageSizeParameter getInstance() {
        if (instance == null) {
            instance = new PageSizeParameter();
        }
        return instance;
    }

    public void apply(final CodegenOperation codegenOperation) {
        if (!Boolean.TRUE.equals(codegenOperation.vendorExtensions.get(SupportedOperation.X_LIST.getValue()))) {
            return;
        }
        // Recorded before the removal below: an operation whose only query parameter is the page size
        // still has to call addQueryParams, otherwise the page size would not be sent at all.
        codegenOperation.vendorExtensions.put(ADD_QUERY_PARAMS, !codegenOperation.queryParams.isEmpty());

        Optional<CodegenParameter> pageSizeParam = codegenOperation.queryParams
                .stream()
                .filter(PageSizeParameter::isDelegatablePageSize)
                .findFirst();
        codegenOperation.vendorExtensions.put(PAGE_SIZE_QUERY_NAME,
                pageSizeParam.map(param -> param.baseName).orElse(DEFAULT_PAGE_SIZE_QUERY_NAME));
        pageSizeParam.ifPresent(param -> {
            codegenOperation.queryParams = codegenOperation.queryParams
                    .stream()
                    .filter(queryParam -> queryParam != param)
                    .collect(Collectors.toList());
            codegenOperation.vendorExtensions.put(PAGE_SIZE_PARAM, param);
        });
    }

    /**
     * A required page size would be a constructor argument, which the reader has to keep a field for, so
     * it is left alone. No spec declares one today.
     */
    private static boolean isDelegatablePageSize(final CodegenParameter param) {
        return PAGE_SIZE_BASE_NAME.equalsIgnoreCase(param.baseName)
                && DELEGATABLE_DATA_TYPES.contains(param.dataType)
                && !param.required;
    }
}
