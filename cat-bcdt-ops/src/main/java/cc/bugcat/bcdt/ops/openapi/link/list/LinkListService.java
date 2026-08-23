package cc.bugcat.bcdt.ops.openapi.link.list;

import cc.bugcat.acore.framework.constants.Errors;
import cc.bugcat.acore.framework.exception.BusinessException;
import cc.bugcat.acore.framework.json.JsonTools;
import cc.bugcat.acore.mybatis.plus.MyWrapper;
import cc.bugcat.acore.webmvc.web.AbstractOpenApiService;
import cc.bugcat.acore.webmvc.web.RequestContext;
import cc.bugcat.acore.framework.utils.BeanCopyUtil;
import cc.bugcat.acore.framework.utils.ObjectUtil;
import cc.bugcat.bcdt.ops.constants.Opers;
import cc.bugcat.bcdt.ops.model.zentao.BcLink;
import cc.bugcat.bcdt.ops.openapi.link.list.vo.LinkColumn;
import cc.bugcat.bcdt.ops.openapi.link.list.vo.LinkGroup;
import cc.bugcat.bcdt.ops.openapi.link.list.vo.LinkInfo;
import cc.bugcat.bcdt.ops.openapi.link.list.vo.LinkLabel;
import cc.bugcat.bcdt.ops.openapi.user.login.vo.UserInfo;
import cc.bugcat.bcdt.ops.service.inter.BcLinkService;
import cc.bugcat.bcdt.ops.utils.UserUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *
 *
 * @author bugcat-cc
 */
@Service
public class LinkListService extends AbstractOpenApiService<String, List<LinkColumn>> {

	@Autowired
	private BcLinkService linkService;

	private static final Pattern linkPat = Pattern.compile("\\{\\{([^\\}\\}]+)\\}\\}");

	private static final String alink = "<a class='el-link el-link--primary' target='${target}' href='${href}' title='${title}' data-title='${data-title}'>${text}</a>";


	@Override
	@Transactional(readOnly = true)
	public List<LinkColumn> execute (String reqVi, RequestContext context) {

		UserInfo user = UserUtil.getUser(context);

		List<BcLink> list = linkService.list(MyWrapper.of(BcLink.class)
				.eq(BcLink::getPageId, reqVi)
				.eq(BcLink::getActive, "1"));


		Map<String, Map<MapKey, Map<MapKey, List<LinkInfo>>>> linkMap = new HashMap<>();
		Map<String, MapKey> mapKey = new HashMap<>();

		Set<String> linkMoreIds = new HashSet<>();
		for ( BcLink link : list ) {

			// 10101000 => 10_1_01_000
			String id = link.getId();
			link.setColId(id.substring(2, 3));
			link.setLabelId(id.substring(3, 5));

			if ( StringUtils.isBlank(link.getType()) ) {
				link.setType("");
			}
			if ( StringUtils.isBlank(link.getRowId()) ) {
				link.setRowId(ObjectUtil.uuid());
			}
			if ( StringUtils.isBlank(link.getExtra()) ) {
				link.setHtmlExtra(new JsonTools());
			} else {
				link.setHtmlExtra(JsonTools.parseObject(link.getExtra()));
			}
			if ( StringUtils.isBlank(link.getTitle()) ) {
				link.setTitle("");
			}
			if ( StringUtils.isBlank(link.getWinTitle()) ) {
				link.setWinTitle("");
			}
			if ( StringUtils.isBlank(link.getClassName()) ) {
				link.setClassName("");
			}
			
			if ( StringUtils.isBlank(link.getAcs()) ) {
				link.setAcs(Opers.login);
			}

			if ( "link-more".equals(link.getType()) ) {
				linkMoreIds.add(link.getId());
			}
			if ( link.getType().startsWith("label") ) {
				MapKey labelKey = new MapKey();
				labelKey.key = link.getLabelId();
				toMapKey(labelKey, link);
				mapKey.put(link.getColId() + "@" + link.getLabelId(), labelKey);
			} else {
				MapKey groupKey = new MapKey();
				groupKey.key = link.getRowId();
				toMapKey(groupKey, link);
				mapKey.put(link.getColId() + "@" + link.getLabelId() + "@" + link.getRowId(), groupKey);
			}
		}

		Map<String, List<BcLink>> linkMoreGroup = linkService.list(MyWrapper.of(BcLink.class)
						.eq(BcLink::getPageId, "---")
						.eq(BcLink::getActive, "1")
						.likeRight(BcLink::getId, linkMoreIds))
						.stream()
						.peek(link -> link.setPageId(link.getId().substring(0, link.getId().length() - 2)))
						.collect(Collectors.groupingBy(BcLink::getPageId));

		for ( BcLink link : list ) {

			if ( link.getType().startsWith("label") ) {
				continue;
			}
			if ( Opers.noOper(user.getOpers(), link.getAcs()) ) {
				continue;
			}

			LinkInfo info = toLinkInfo(link, linkMoreGroup);

			Map<MapKey, Map<MapKey, List<LinkInfo>>> columnMap = linkMap.get(link.getColId());
			if ( columnMap == null ) {
				columnMap = new HashMap<>();
				linkMap.put(link.getColId(), columnMap);
			}

			MapKey labelKey = mapKey.get(link.getColId() + "@" + link.getLabelId());
			if ( labelKey == null ) {
				throw BusinessException.builder(Errors.SYS_ERR, "未获取到label=" +  link.getLabelId() + "的数据！");
			}
			Map<MapKey, List<LinkInfo>> labelMap = columnMap.get(labelKey);
			if ( labelMap == null ) {
				labelMap = new HashMap<>();
				columnMap.put(labelKey, labelMap);
			}

			MapKey groupKey = mapKey.get(link.getColId() + "@" + link.getLabelId() + "@" + link.getRowId());
			List<LinkInfo> groupList = labelMap.get(groupKey);
			if ( groupList == null ) {
				groupList = new ArrayList<>();
				labelMap.put(groupKey, groupList);
			}

			groupList.add(info);
		}

		Map<String, LinkColumn> respMap = new HashMap<>();
		for ( Map.Entry<String, Map<MapKey, Map<MapKey, List<LinkInfo>>>> entry : linkMap.entrySet() ) {
			String colId = entry.getKey();
			List<LinkLabel> labelValue = toLinkLabel(entry.getValue());

			LinkColumn linkColumn = new LinkColumn();
			linkColumn.setColumnName(colId);
			linkColumn.setLabelValue(labelValue);

			respMap.put(colId, linkColumn);
		}

		List<LinkColumn> resp = toList(respMap, "1", "2", "3");
		return resp;
	}



	private static LinkInfo toLinkInfo(BcLink link, Map<String, List<BcLink>> linkMoreGroup) {


		LinkInfo info = BeanCopyUtil.copy(link, LinkInfo::new);
		info.setExtra(link.getHtmlExtra());

		// 纯文字链接，文字 + 超链接
		if ( link.getType().startsWith("link") ) {
			if ( StringUtils.isNoneBlank(link.getLinkUrl(), link.getLinkUrl()) ) {
				String name = link.getLabel().trim();
				if ( name.contains("{{") ) {
					final String[] urls = StringUtils.split(link.getLinkUrl(), "|");
					for ( int idx = 0; idx < urls.length; idx ++ ) {
						Matcher matcher = linkPat.matcher(name);
						if ( !matcher.find() ) {
							break;
						}
						String url = StringUtils.trim(urls[idx]);
						Map<String, String> argsMap = new HashMap<>();
						argsMap.put("target", url.startsWith("http") ? "_blank" : "");
						argsMap.put("href", url);
						argsMap.put("title", link.getTitle());
						argsMap.put("data-title", link.getWinTitle());
						argsMap.put("text", matcher.group(1));
						name = matcher.replaceFirst("</span>" + (formtLink(alink, argsMap)) + "<span>");
					}
					name = ("<span>" + name + "</span>")
							.replaceAll("<span>\\s*</span>", "")
							.replaceAll("<span>\\s*\\[", "<span class='link-span'>[");
				} else {

					Map<String, String> argsMap = new HashMap<>();
					argsMap.put("target", link.getLinkUrl().startsWith("http") ? "_blank" : "");
					argsMap.put("href", link.getLinkUrl());
					argsMap.put("title", link.getTitle());
					argsMap.put("data-title", link.getWinTitle());
					argsMap.put("text", name);
					name = formtLink(alink, argsMap);
				}
				info.setLabel(name);
			}
			if ( "link-more".equals(link.getType()) ) {
				List<BcLink> moreList = linkMoreGroup.get(link.getId());
				List<LinkInfo> mores = Optional.ofNullable(moreList).orElseGet(Collections::emptyList)
						.stream().map(item -> toLinkInfo(item, linkMoreGroup)).collect(Collectors.toList());
				info.setLinkMore(mores);
			}
		}
		return info;
	}



	private static List<LinkLabel> toLinkLabel(Map<MapKey, Map<MapKey, List<LinkInfo>>> columnMap) {

		List<LinkLabel> labelValue = new ArrayList<>(columnMap.size());
		for ( Map.Entry<MapKey, Map<MapKey, List<LinkInfo>>> entry : columnMap.entrySet() ) {
			MapKey labelKey = entry.getKey();
			List<LinkGroup> groupList = toLinkGroup(entry.getValue());
			LinkLabel linkLabel = new LinkLabel();
			linkLabel.setLabelName(labelKey.name);
			linkLabel.setClassName(labelKey.className);
			linkLabel.setType(labelKey.type);
			linkLabel.setId(labelKey.id);
			linkLabel.setExtra(labelKey.extra);
			linkLabel.setGroupValue(groupList);
			labelValue.add(linkLabel);
		}
		labelValue.sort(Comparator.comparing(LinkLabel::getId));
		return labelValue;
	}

	private static List<LinkGroup> toLinkGroup(Map<MapKey, List<LinkInfo>> groupMap) {
		List<LinkGroup> groupList = new ArrayList<>(groupMap.size());
		for ( Map.Entry<MapKey, List<LinkInfo>> entry : groupMap.entrySet() ) {
			MapKey groupKey = entry.getKey();
			List<LinkInfo> infoList = entry.getValue();
			infoList.sort(Comparator.comparing(LinkInfo::getId));

			LinkGroup linkGroup = new LinkGroup();
			linkGroup.setId(groupKey.id);
			linkGroup.setInfoValue(infoList);
			groupList.add(linkGroup);
		}

		groupList.sort(Comparator.comparing(LinkGroup::getId));
		return groupList;
	}

	private static List<LinkColumn> toList(Map<String, LinkColumn> columnMap, String... columnIds) {
		List<LinkColumn> list = new ArrayList<>(columnMap.size());
		for( String colId : columnIds ) {
			LinkColumn link = columnMap.get(colId);
			if ( link != null ) {
				list.add(link);
			}
		}
		return list;
	}

	private static void toMapKey(MapKey mapKey, BcLink link){
		mapKey.id = link.getId();
		mapKey.name = link.getLabel();
		mapKey.type = link.getType();
		mapKey.className = link.getClassName();
		mapKey.extra = link.getHtmlExtra();
	}



	public static class MapKey {
		private String id;
		private String key;
		private String name;
		private String type;
		private String className;
		private JsonTools extra;

		@Override
		public boolean equals(Object other) {
			if (this == other) return true;
			if (other == null || getClass() != other.getClass()) return false;
			MapKey labelKey = (MapKey) other;
			return Objects.equals(key, labelKey.key);
		}

		@Override
		public int hashCode() {
			return Objects.hash(key);
		}
	}


	private static String formtLink(String link, Map<String, String> args) {
		Pattern linkPat = Pattern.compile("\\$\\{([^\\}]+)\\}");
		Matcher matcher = linkPat.matcher(link);
		while ( matcher.find() ) {
			String key = matcher.group(1);
			String value = args.getOrDefault(key, "");
			link = link.replace("${" + key + "}", StringUtils.trimToEmpty(value));
		}
		return link;
	}

}