/**
 * Copyright 2018 The original authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.dekorate.kubernetes.decorator;

import io.dekorate.WithProject;
import io.dekorate.doc.Description;
import io.dekorate.project.Project;
import io.dekorate.utils.Annotations;
import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.ObjectMetaBuilder;

/**
 * A decorator that adds a label to resources.
 */
@Description("Add a vcs uri label to the all metadata.")
public class AddCommitIdAnnotationDecorator extends NamedResourceDecorator<ObjectMetaBuilder> implements WithProject {

  private final String annotationKey;

  public AddCommitIdAnnotationDecorator() {
    this(ANY);
  }

  public AddCommitIdAnnotationDecorator(String name) {
    this(name, Annotations.COMMIT_ID);
  }

  public AddCommitIdAnnotationDecorator(String name, String annotationKey) {
    super(name);
    this.annotationKey = annotationKey;
  }

  @Override
  public void andThenVisit(ObjectMetaBuilder builder, ObjectMeta resourceMeta) {
    Project p = getProject();
    boolean hasCommit = p.getScmInfo() != null && p.getScmInfo().getCommit() != null;
    if (hasCommit) {
      builder.addToAnnotations(annotationKey, getProject().getScmInfo().getCommit());
    }
  }

  public String getAnnotationKey() {
    return annotationKey;
  }

  @Override
  public Class<? extends Decorator>[] before() {
    return new Class[] { RemoveAnnotationDecorator.class };
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1 + getClass().hashCode();
    result = prime * result + ((annotationKey == null) ? 0 : annotationKey.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    AddCommitIdAnnotationDecorator other = (AddCommitIdAnnotationDecorator) obj;
    if (annotationKey == null) {
      if (other.annotationKey != null)
        return false;
    } else if (!annotationKey.equals(other.annotationKey))
      return false;
    return true;
  }
}
