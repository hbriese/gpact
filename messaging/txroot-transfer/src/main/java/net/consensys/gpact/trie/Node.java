/*
 * Copyright ConsenSys Software Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package net.consensys.gpact.trie;

import java.util.List;
import java.util.Optional;
import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;

public interface Node<V> {

  Node<V> accept(PathNodeVisitor<V> visitor, Bytes path);

  default Node<V> constructMultiproof(final List<Bytes> keys, final NodeFactory<V> nodeFactory) {
    return NullNode.instance();
  }

  Bytes constructSimpleProof(final Bytes key, final List<Bytes> proof);

  void accept(NodeVisitor<V> visitor);

  Bytes getPath();

  Optional<V> getValue();

  List<Node<V>> getChildren();

  Bytes getRlp();

  Bytes getRlpRef();

  /**
   * Whether a reference to this node should be represented as a hash of the rlp, or the node rlp
   * itself should be inlined (the rlp stored directly in the parent node). If true, the node is
   * referenced by hash. If false, the node is referenced by its rlp-encoded value.
   *
   * @return true if this node should be referenced by hash
   */
  default boolean isReferencedByHash() {
    return getRlp().size() >= 32;
  }

  Bytes32 getHash();

  Node<V> replacePath(Bytes path);

  /** Marks the node as needing to be persisted */
  void markDirty();

  /** @return True if the node needs to be persisted. */
  boolean isDirty();

  String print();

  /** Unloads the node if it is, for example, a StoredNode. */
  default void unload() {}
}
